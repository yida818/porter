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
package cn.vbill.middleware.porter.manager.cluster.zookeeper;

import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.task.statistics.DTaskPerformance;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.service.MrJobTasksMonitorService;
import cn.vbill.middleware.porter.manager.service.MrNodesMonitorService;
import cn.vbill.middleware.porter.manager.service.impl.MrJobTasksMonitorServiceImpl;
import cn.vbill.middleware.porter.manager.service.impl.MrNodesMonitorServiceImpl;

/**
 * 统计监控
 * 
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class ZKClusterStatisticListener extends ZookeeperClusterListener {

    private static final String ZK_PATH = BASE_CATALOG + "/statistic";
    private static final Pattern TASK_PATTERN = Pattern.compile(ZK_PATH + "/task/.*");

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent zkEvent) {
        String zkPath = zkEvent.getId();
        logger.debug("StatisticListener:{},{},{}", zkPath, zkEvent.getData(), zkEvent.getEventType());
        if (zkEvent.isOnline() && TASK_PATTERN.matcher(zkPath).matches()) {
            try {
                // 性能指标数据
                DTaskPerformance performance = JSONObject.parseObject(zkEvent.getData(), DTaskPerformance.class);
                if (performance == null) {
                    logger.error("cluster-DTaskPerformance....." + JSON.toJSON(performance));
                } else {
                    logger.info("cluster-DTaskPerformance....." + JSON.toJSON(performance));
                    // do something
                    try {
                        // 任务泳道实时监控表 服务接口类
                        MrJobTasksMonitorService mrJobTasksMonitorService = ApplicationContextUtil
                                .getBean(MrJobTasksMonitorServiceImpl.class);
                        mrJobTasksMonitorService.dealTaskPerformance(performance);
                        // 节点任务实时监控表
                        MrNodesMonitorService mrNodesMonitorService = ApplicationContextUtil
                                .getBean(MrNodesMonitorServiceImpl.class);
                        mrNodesMonitorService.dealTaskPerformance(performance);
                    } catch (Exception e) {
                        logger.error("cluster-DTaskPerformance-Error....出错,请追寻...", e);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                logger.error("cluster-DTaskPerformance-Error....出错,请追寻...", e);
            } finally {
                // 删除已获取的事件
                client.delete(zkPath);
            }
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ClusterListenerFilter() {
            @Override
            public String getPath() {
                return listenPath();
            }

            @Override
            public boolean doFilter(ClusterTreeNodeEvent event) {
                return true;
            }
        };
    }

    @Override
    public void start() {
        client.create(ZK_PATH, null, false, true);
        client.create(ZK_PATH + "/task", null, false, true);
    }
}
