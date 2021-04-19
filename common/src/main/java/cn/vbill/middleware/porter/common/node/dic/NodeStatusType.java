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

package cn.vbill.middleware.porter.common.node.dic;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 节点任务推送状态
 * 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 10:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 10:32
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NodeStatusType {

    /**
     * 工作中
     */
    WORKING("WORKING", "工作中"),

    /**
     * 已暂停
     */
    SUSPEND("SUSPEND", "已暂停");

    /**
     * LINKMAP
     */
    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<>();
    static {
        LINKMAP.put(WORKING.code, WORKING.name);
        LINKMAP.put(SUSPEND.code, SUSPEND.name);
    }

    private final String code;
    private final String name;

    public boolean isSuspend() {
        return this == SUSPEND;
    }

    public boolean isWorking() {
        return this == WORKING;
    }

    NodeStatusType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
