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

package cn.vbill.middleware.porter.common.cluster;

import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;

import java.util.List;
import java.util.Map;

/**
 * 集群监听
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:45
 */
public interface ClusterMonitor {
    /**
     * 添加监听器
     * @param listener
     */
    void registerListener(List<ClusterListener> listener);

    /**
     * 设置集群提供客户端
     * @param client
     */
    void setClient(ClusterClient client);

    /**
     *
     * @param e
     */
    void onEvent(ClusterTreeNodeEvent e);

    /**
     * 获取监听器
     * @return
     */
    Map<String, ClusterListener> getListener();

    /**
     * 启动监听
     */
    void start() throws InterruptedException;

    /**
     * 停止监听
     * @throws Exception
     */
    void stop();

    void noticeClusterListenerEvent(ClusterCommand command);
    void registerClusterEvent(ClusterListenerEventExecutor eventExecutor);
}
