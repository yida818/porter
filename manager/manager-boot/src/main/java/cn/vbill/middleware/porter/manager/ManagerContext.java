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

package cn.vbill.middleware.porter.manager;

import cn.vbill.middleware.porter.common.warning.entity.WarningMessage;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 16:03
 */
@SuppressWarnings("unchecked")
public enum ManagerContext {

    /**
     * INSTANCE
     */
    INSTANCE();

    private ApplicationContext context;
    private final Map<List<String>, WarningMessage> taskErrorMarked = new ConcurrentHashMap<>();
    private volatile List<WarningReceiver> receivers = new ArrayList<>();

    /**
     * 获取Bean
     *
     * @date 2018/8/9 下午4:14
     * @param: [clazz]
     * @return: T
     */
    public <T> T getBean(Class<T> clazz) {
        return null != context ? context.getBean(clazz) : null;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    /**
     * newStoppedTask
     *
     * @date 2018/8/9 下午4:15
     * @param: [taskId, swimlaneId]
     * @return: void
     */
    public void newStoppedTask(List<String> key, WarningMessage message) {
        taskErrorMarked.put(key, message);
    }

    /**
     * removeStoppedTask
     *
     * @date 2018/8/9 下午4:15
     * @param: [taskId, swimlaneId]
     * @return: void
     */
    public void removeStoppedTask(List<String> key) {
        taskErrorMarked.remove(key);
    }

    public List<String> getStoppedTasks() {
        return taskErrorMarked.values().stream().map(WarningMessage::getTitle).collect(Collectors.toList());
    }



    public synchronized void addWarningReceivers(WarningReceiver[] newReceivers) {
        receivers.addAll(Arrays.asList(newReceivers));
    }

    public List<WarningReceiver> getReceivers() {
        return receivers;
    }
}
