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
import cn.vbill.middleware.porter.common.node.statistics.NodeLog;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.service.MrLogMonitorService;
import cn.vbill.middleware.porter.manager.service.impl.MrLogMonitorServiceImpl;

/**
 * 任务日志监控
 *
 * @author: guohongjian[wszghj@aliyun.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: guohongjian[wszghj@aliyun.com]/2017年12月15日 10:09
 */
public class ZKClusterStatisticListener extends ZookeeperClusterListener {
    private static final String ZK_PATH = BASE_CATALOG + "/statistic";
    private static final Pattern LOG_PATTERN = Pattern.compile(ZK_PATH + "/log/.*");

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent zkEvent) {
        String zkPath = zkEvent.getId();
        logger.debug("StatisticListener:{},{},{}", zkPath, zkEvent.getData(), zkEvent.getEventType());
        if (zkEvent.isOnline() && LOG_PATTERN.matcher(zkPath).matches()) {
            try {
                NodeLog log = JSONObject.parseObject(zkEvent.getData(), NodeLog.class);
                if (log == null) {
                    logger.error("3-boot-NodeLog....." + JSON.toJSON(log));
                } else {
                    logger.info("3-boot-NodeLog....." + JSON.toJSON(log));
                    // do something
                    try {
                        MrLogMonitorService mrLogMonitorService = ApplicationContextUtil
                                .getBean(MrLogMonitorServiceImpl.class);
                        mrLogMonitorService.dealNodeLog(log);
                    } catch (Exception e) {
                        logger.error("3-boot-NodeLog-Error....出错,请追寻...", e);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                logger.error("3-boot-DTaskPerformance-Error....出错,请追寻...", e);
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
        client.create(ZK_PATH + "/log", null, false, true);
    }
}