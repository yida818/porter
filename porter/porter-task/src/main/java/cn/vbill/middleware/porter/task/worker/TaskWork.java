/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.task.worker;

import cn.vbill.middleware.porter.common.cluster.event.command.*;
import cn.vbill.middleware.porter.common.warning.entity.WarningOwner;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.statistics.DCallback;
import cn.vbill.middleware.porter.common.statistics.DObject;
import cn.vbill.middleware.porter.common.task.statistics.DTaskStat;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.node.statistics.NodeLog;
import cn.vbill.middleware.porter.common.task.statistics.DTaskPerformance;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.task.TaskContext;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.task.loader.DataLoader;
import cn.vbill.middleware.porter.core.task.job.StageJob;
import cn.vbill.middleware.porter.core.task.job.StageType;
import cn.vbill.middleware.porter.core.task.entity.TableMapper;
import cn.vbill.middleware.porter.task.alert.AlertJob;
import cn.vbill.middleware.porter.task.extract.ExtractJob;
import cn.vbill.middleware.porter.task.load.LoadJob;
import cn.vbill.middleware.porter.task.select.SelectJob;
import cn.vbill.middleware.porter.task.transform.TransformJob;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 任务主线程
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 14:48
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 14:48
 */
public class TaskWork extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWork.class);
    //任务配置信息
    private final String taskId;
    private final DataConsumer dataConsumer;
    private final DataLoader dataLoader;
    private final Map<String, TableMapper> mappers;
    private final List<WarningReceiver> receivers;

    //stageType -> job
    private final Map<StageType, StageJob> stageJobs = new LinkedHashMap<>();
    //schema_table -> TaskStat
    private final Map<String, DTaskStat> stats;
    private final ArrayBlockingQueue<SignalType> threadSignal = new ArrayBlockingQueue<>(1);
    /**
     * 触发任务停止标识，生命周期内，仅有一次
     */
    private final AtomicBoolean stopTrigger;

    private final String basicThreadName;
    private final TaskWorker worker;
    private final long positionCheckInterval;
    private final long alarmPositionCount;

    public TaskWork(DataConsumer dataConsumer, DataLoader dataLoader, String taskId, List<WarningReceiver> receivers,
                    TaskWorker worker, long positionCheckInterval, long alarmPositionCount) {
        basicThreadName = "TaskWork-[taskId:" + taskId + "]-[consumer:" + dataConsumer.getSwimlaneId() + "]";
        setName(basicThreadName + "-main");
        this.dataConsumer = dataConsumer;
        this.dataLoader = dataLoader;
        this.taskId = taskId;
        this.stats = new ConcurrentHashMap<>();
        this.mappers = new ConcurrentHashMap<>();
        this.worker = worker;
        this.receivers = Collections.unmodifiableList(receivers);
        this.positionCheckInterval = positionCheckInterval;
        this.alarmPositionCount = alarmPositionCount;
        this.stopTrigger = new AtomicBoolean(false);
    }

    private void initiate() {
        stageJobs.put(StageType.SELECT, new SelectJob(this));
        stageJobs.put(StageType.EXTRACT, new ExtractJob(this));
        stageJobs.put(StageType.TRANSFORM, new TransformJob(this));
        stageJobs.put(StageType.LOAD, new LoadJob(this, positionCheckInterval, alarmPositionCount));
        /**
         * 源端数据源支持元数据查询
         */
        if (dataConsumer.supportMetaQuery()) {
            stageJobs.put(StageType.DB_CHECK, new AlertJob(this));
        }
    }

    public void stopWork() {
        threadSignal.clear();
        try {
            threadSignal.put(SignalType.STOP);
        } catch (InterruptedException e) {
            stopWork();
        }
    }

    public void transmit() {
        threadSignal.offer(SignalType.TRANSMIT);
    }

    public String getSignal() throws InterruptedException {
        return threadSignal.take().name();
    }

    /**
     * 主线程逻辑
     */
    public void run() {
        try {
            TaskContext.trace(taskId, dataConsumer, dataLoader, receivers);
            initiate();
            LOGGER.info("从注册中心拉取任务状态信息[{}-{}]", taskId, dataConsumer.getSwimlaneId());
            //从集群模块获取任务状态统计信息
            ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskStatQueryCommand(taskId, dataConsumer.getSwimlaneId(), new DCallback() {
                @Override
                public void callback(List<DObject> objects) {
                    for (DObject object : objects) {
                        DTaskStat stat = (DTaskStat) object;
                        getDTaskStat(stat.getSchema(), stat.getTable());
                    }
                }
            }));
            LOGGER.info("启动StageJob[{}-{}]", taskId, dataConsumer.getSwimlaneId());
            //开始阶段性工作
            for (Map.Entry<StageType, StageJob> jobs : stageJobs.entrySet()) {
                jobs.getValue().start();
            }
            LOGGER.info("开始获取任务消费泳道[{}-{}]上次同步点", taskId, dataConsumer.getSwimlaneId());
            //获取上次任务进度
            ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskPositionQueryCommand(taskId, dataConsumer.getSwimlaneId(), new DCallback() {
                @Override
                @SneakyThrows(TaskStopTriggerException.class)
                public void callback(String position) {
                    LOGGER.info("获取任务消费泳道[{}-{}]上次同步点->{}，通知SelectJob", taskId, dataConsumer.getSwimlaneId(), position);
                    position = StringUtils.isBlank(position) ? dataConsumer.getInitiatePosition() : position;
                    LOGGER.info("计算任务消费泳道[{}-{}]最终同步点->{}，通知SelectJob", taskId, dataConsumer.getSwimlaneId(), position);
                    dataConsumer.initializePosition(taskId, dataConsumer.getSwimlaneId(), position);
                }
            }));

            //查询任务关联告警人信息
            ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskOwnerQueryCommand(taskId, new DCallback() {
                @Override
                public void callback(String owner) {
                    TaskContext.trace(JSONObject.parseObject(owner, WarningOwner.class));
                }
            }));

            while (true) {
                String signalType = null;
                try {
                    //获取信号量当前状态
                    signalType = getSignal();
                    LOGGER.info("任务信号量[{}-{}]:{}", taskId, dataConsumer.getSwimlaneId(), signalType);
                } catch (InterruptedException e) {
                    LOGGER.info("线程中断，任务停止[{}-{}]", taskId, dataConsumer.getSwimlaneId());
                    break;
                }

                //如果当前获取信号量是线程间通信造成的，保持线程不退出
                if (null != signalType && signalType.equals(SignalType.TRANSMIT.name())) {
                    TaskContext.trace(worker.getOwner());
                    continue;
                } else {
                    break;
                }
            }
        } catch (Throwable e) {
            LOGGER.error("任务[{}-{}]停止", taskId, dataConsumer.getSwimlaneId(), e);
            TaskContext.warning(NodeLog.upload(NodeLog.LogType.WARNING, taskId, dataConsumer.getSwimlaneId(), e.getMessage()));
            interruptWithWarning(e.getMessage());
        } finally {
            clearWork();
        }
    }

    /**
     * 清理任务
     */
    private void clearWork() {
        try {
            worker.unregister(dataConsumer.getSwimlaneId());
            LOGGER.info("终止执行任务[{}-{}]", taskId, dataConsumer.getSwimlaneId());
            //终止阶段性工作,需要
            for (Map.Entry<StageType, StageJob> jobs : stageJobs.entrySet()) {
                //确保每个阶段工作都被执行
                try {
                    LOGGER.info("终止执行工作[{}-{}-{}]", taskId, dataConsumer.getSwimlaneId(), jobs.getValue().getClass().getSimpleName());
                    jobs.getValue().stop();
                } catch (Throwable e) {
                    LOGGER.error("终止执行工作[{}-{}-{}]失败", taskId, dataConsumer.getSwimlaneId(), jobs.getValue().getClass().getSimpleName(), e);
                }
            }
            try {
                //广播任务结束消息
                ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskStopCommand(taskId, dataConsumer.getSwimlaneId()));
            } catch (Exception e) {
                TaskContext.warning(NodeLog.upload(NodeLog.LogType.INFO, taskId, dataConsumer.getSwimlaneId(), "广播TaskStopCommand失败:" + e.getMessage()));
            }
        } catch (Throwable e) {
            TaskContext.warning(NodeLog.upload(NodeLog.LogType.INFO, taskId, dataConsumer.getSwimlaneId(), "任务关闭失败:" + e.getMessage()));
            LOGGER.error("终止执行任务[{}-{}]异常", taskId, dataConsumer.getSwimlaneId(), e);
        } finally {
            TaskContext.clearTrace();
            NodeContext.INSTANCE.clearConsumeProcess(taskId, dataConsumer.getSwimlaneId());
            NodeContext.INSTANCE.flushConsumerIdle(taskId, dataConsumer.getSwimlaneId(), -1);
        }
    }


    public String getBasicThreadName() {
        return basicThreadName;
    }

    /**
     * 等待Event
     *
     * @date 2018/8/9 下午2:15
     * @param: [type]
     * @return: T
     */
    public <T> T waitEvent(StageType type) throws TaskStopTriggerException, InterruptedException {
        return stageJobs.get(type).output();
    }

    /**
     * 等待Sequence
     *
     * @date 2018/8/9 下午2:16
     * @param: []
     * @return: T
     */
    public <T> T waitSequence() throws InterruptedException {
        return ((ExtractJob) stageJobs.get(StageType.EXTRACT)).getNextSequence();
    }

    /**
     * isPoolEmpty
     *
     * @date 2018/8/9 下午2:16
     * @param: [type]
     * @return: boolean
     */
    public boolean isPoolEmpty(StageType type) {
        return stageJobs.get(type).isPoolEmpty();
    }

    public String getTaskId() {
        return taskId;
    }

    /**
     * submitStat
     *
     * @date 2018/8/9 下午2:16
     * @param: []
     * @return: void
     */
    public void submitStat() {
        stats.forEach((s, stat) -> {
            LOGGER.debug("stat before submit:{}", JSON.toJSONString(stat));
            //多线程访问情况下（目前是两个线程:状态上报线程、任务状态更新线程），获取JOB的运行状态。
            DTaskStat newStat = null;
            synchronized (stat) {
                newStat = stat.snapshot(DTaskStat.class);
                stat.reset();
            }
            LOGGER.debug("stat snapshot:{}", JSON.toJSONString(newStat));
            try {
                ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskStatCommand(newStat, new DCallback() {
                    @Override
                    public void callback(DObject object) {
                        DTaskStat remoteData = (DTaskStat) object;
                        synchronized (stat) {
                            if (stat.getUpdateStat().compareAndSet(false, true)) {
                                //最后检查点
                                if (null == stat.getLastCheckedTime()) {
                                    stat.setLastLoadedDataTime(remoteData.getLastLoadedDataTime());
                                }
                                //最初启动时间
                                if (null != remoteData.getRegisteredTime()) {
                                    stat.setRegisteredTime(remoteData.getRegisteredTime());
                                }
                            }
                        }
                    }
                }));
            } catch (Throwable e) {
                NodeLog.upload(NodeLog.LogType.INFO, taskId, dataConsumer.getSwimlaneId(), "上传任务状态信息失败:" + e.getMessage());
            }

            //上传统计
            try {
                //DTaskPerformance
                if (NodeContext.INSTANCE.isUploadStatistic()) {
                    ClusterProviderProxy.INSTANCE.broadcastEvent(new StatisticUploadCommand(new DTaskPerformance(newStat)));
                }
            } catch (Throwable e) {
                TaskContext.warning(NodeLog.upload(NodeLog.LogType.INFO, taskId, dataConsumer.getSwimlaneId(), "上传任务统计信息失败:" + e.getMessage()));
            }
        });
    }

    /**
     * 获取TableMapper
     *
     * @date 2018/8/9 下午2:17
     * @param: [schema, table]
     * @return: cn.vbill.middleware.porter.core.task.entity.TableMapper
     */
    public TableMapper getTableMapper(String schema, String table) {
        String key = schema + "." + table;
        TableMapper mapper = mappers.computeIfAbsent(key, s -> {
            TableMapper tmp = null;
            String mapperKey = taskId + "_" + schema + "_" + table;
            tmp = worker.getTableMapper().get(mapperKey);
            if (null == tmp) {
                mapperKey = taskId + "__" + table;
                tmp = worker.getTableMapper().get(mapperKey);
            }
            if (null == tmp) {
                mapperKey = taskId + "_" + schema + "_";
                tmp = worker.getTableMapper().get(mapperKey);
            }
            if (null == tmp) {
                mapperKey = taskId + "_" + "_";
                tmp = worker.getTableMapper().get(mapperKey);
            }
            return tmp;
        });
        return mapper;
    }

    /**
     * 获取TaskStat
     *
     * @date 2018/8/9 下午2:18
     * @param: [schema, table]
     * @return: cn.vbill.middleware.porter.common.cluster.data.DTaskStat
     */
    public DTaskStat getDTaskStat(String schema, String table) {
        String key = schema + "." + table;
        DTaskStat stat = stats.computeIfAbsent(key, s ->
                new DTaskStat(taskId, null, dataConsumer.getSwimlaneId(), schema, table)
        );
        return stat;
    }

    public List<DTaskStat> getStats() {
        return Collections.unmodifiableList(stats.values().stream().collect(Collectors.toList()));
    }

    /**
     * interruptWithWarning
     * @date 2018/8/9 下午2:18
     * @param: [notice]
     * @return: void
     */
    public void interruptWithWarning(final String notice) {
        if (stopTrigger.compareAndSet(false, true)) {
            new Thread("suixingpay-TaskStopByErrorTrigger-stopTask-" + taskId + "-" + dataConsumer.getSwimlaneId()) {
                @Override
                public void run() {
                    //1.构造日志及告警信息
                    NodeLog log = new NodeLog(NodeLog.LogType.ERROR, notice);
                    String warning = notice;
                    try {
                        //构造日志
                        String logContent = new StringBuilder(notice).append(System.lineSeparator()).append("source:").append(dataConsumer.getClientInfo())
                                .append(System.lineSeparator()).append("target:").append(dataLoader.getClientInfo()).toString();
                        ////上传日志
                        log = new NodeLog(NodeLog.LogType.ERROR, taskId, dataConsumer.getSwimlaneId(), logContent).bindRelationship(TaskContext.taskOwnerInfo());
                        //构造告警信息
                        warning = JSONObject.toJSONString(TaskContext.warning(log));
                    } catch (Throwable e) {
                    }

                    //2.标记任务异常停止
                    try {
                        LOGGER.info("开始标记错误告警.....");
                        ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskStoppedByErrorCommand(taskId, dataConsumer.getSwimlaneId(), warning));
                        LOGGER.info("结束标记错误告警.....");
                    } catch (Throwable e) {
                        LOGGER.error("标记错误告警失败", e);
                    }

                    //3.停止任务
                    try {
                        worker.stopWork(taskId, dataConsumer.getSwimlaneId());
                    } catch (Throwable e) {
                        LOGGER.error("停止任务失败", e);
                    }

                    //4.上传运行态错误日志
                    try {
                        LOGGER.info("开始上传错误日志.....");
                        log.upload();
                        LOGGER.info("结束上传错误日志.....");
                    } catch (Throwable e) {
                        LOGGER.error("上传错误日志失败", e);
                    }
                }
            }.start();
        }
    }

    public DataConsumer getDataConsumer() {
        return dataConsumer;
    }

    public DataLoader getDataLoader() {
        return dataLoader;
    }

    public List<WarningReceiver> getReceivers() {
        return receivers;
    }

    public boolean isWorking() {
        return !stopTrigger.get();
    }

    public enum SignalType {
        STOP, TRANSMIT
    }
}
