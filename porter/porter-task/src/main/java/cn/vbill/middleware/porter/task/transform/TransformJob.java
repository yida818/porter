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

package cn.vbill.middleware.porter.task.transform;

import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.task.job.StageType;
import cn.vbill.middleware.porter.datacarrier.simple.FixedCapacityCarrier;
import cn.vbill.middleware.porter.task.transform.transformer.TransformFactory;
import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.core.task.job.AbstractStageJob;
import cn.vbill.middleware.porter.datacarrier.DataMapCarrier;
import cn.vbill.middleware.porter.task.worker.TaskWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 多线程执行,完成字段、表的映射转化。
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:32
 */
public class TransformJob extends AbstractStageJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformJob.class);

    private final TransformFactory transformFactory;
    private final ExecutorService executorService;
    //容量为线程池容量的100倍
    private final DataMapCarrier<String, Future<ETLBucket>> carrier = new FixedCapacityCarrier(JOB_THREAD_SIZE * 100);
    private final TaskWork work;

    //工作线程数量
    private static final int JOB_THREAD_SIZE = 1;

    public TransformJob(TaskWork work) {
        super(work.getBasicThreadName(), 50L);
        this.work = work;
        transformFactory = NodeContext.INSTANCE.getBean(TransformFactory.class);
        //线程阻塞时，在调用者线程中执行
        executorService = new ThreadPoolExecutor(JOB_THREAD_SIZE, JOB_THREAD_SIZE * 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(JOB_THREAD_SIZE * 5),
                getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    protected void doStop() {
        executorService.shutdown();
    }

    @Override
    protected void doStart() {

    }
    @Override
    protected void loopLogic() throws InterruptedException {
        //只要队列有消息，持续读取
        while (getWorkingStat()) {
            try {
                ETLBucket bucket = work.waitEvent(StageType.EXTRACT);
                if (null != bucket) {
                    LOGGER.debug("transform ETLBucket batch {} begin.", bucket.getSequence());
                    final ETLBucket inThreadBucket = bucket;
                    Future<ETLBucket> result = executorService.submit(() -> {
                        //上个流程处理没有异常
                        try {
                            if (null == inThreadBucket.getException()) {
                                transformFactory.transform(inThreadBucket, work);
                            }
                        } catch (Throwable stopError) {
                            LOGGER.error("批次[{}]执行TransformJob失败!", inThreadBucket.getSequence(), stopError);
                            work.interruptWithWarning(stopError.getMessage());
                            executorService.shutdownNow();
                        }
                        return inThreadBucket;
                    });
                    if (!getWorkingStat() && !work.isWorking()) break;
                    LOGGER.debug("transform ETLBucket batch {} end.", bucket.getSequence());
                    carrier.push(inThreadBucket.getSequence(), result);
                    carrier.printState();
                }
            } catch (TaskStopTriggerException stopError) {
                LOGGER.error("TransformJob error", stopError);
                work.interruptWithWarning(stopError.getMessage());
                break;
            }
        }
    }

    @Override
    public ETLBucket output() throws InterruptedException, TaskStopTriggerException {
        try {
            String sequence = work.waitSequence();
            Future<ETLBucket> result = null != sequence ? carrier.pull(sequence) : null;
            return null != result ? result.get() : null;
        } catch (ExecutionException e) {
            throw new TaskStopTriggerException(e);
        }
    }

    @Override
    public boolean isPoolEmpty() {
        return carrier.size() == 0 && executorService.isTerminated();
    }

    @Override
    public boolean isPrevPoolEmpty() {
        return work.isPoolEmpty(StageType.EXTRACT);
    }

    @Override
    public boolean stopWaiting() {
        return work.getDataConsumer().isAutoCommitPosition();
    }

    @Override
    protected boolean workingStat() {
        return work.isWorking();
    }
}
