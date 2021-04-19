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

package cn.vbill.middleware.porter.common.cluster.event.command;

import cn.vbill.middleware.porter.common.statistics.DCallback;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import lombok.Getter;

/**
 * 任务状态查询（服务查询上报zk）
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月12日 18:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月12日 18:46
 */
public class TaskStatQueryCommand extends ClusterCommand {
    @Getter private final String taskId;
    @Getter private final String swimlaneId;
    @Getter private final DCallback callback;

    public TaskStatQueryCommand(String taskId, String swimlaneId, DCallback dCallback) {
        this.taskId = taskId;
        this.swimlaneId = swimlaneId;
        this.callback = dCallback;
    }

    @Override
    public ClusterListenerEventType getClusterListenerEventType() {
        return ClusterListenerEventType.TaskStatQuery;
    }
}
