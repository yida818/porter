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

package cn.vbill.middleware.porter.manager.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 任务权限操作类型
 *
 * @author MurasakiSeiFu
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ControlTypeEnum {

    /**
     * CHANGE
     */
    CHANGE("CHANGE", "移交"),

    /**
     * SHARE
     */
    SHARE("SHARE", "共享"),

    /**
     * CANCEL
     */
    CANCEL("CANCEL", "作废"),

    /**
     * RECYCLE
     */
    RECYCLE_C("RECYCLE_C", "回收所有者"),

    /**
     * RECYCLE
     */
    RECYCLE_S("RECYCLE_S", "回收共享者"),

    /**
     * RECYCLE
     */
    RECYCLE_A("RECYCLE_A", "回收所有权限");

    @Getter
    private final String code;

    @Getter
    private final String name;

    /**
     * LINKMAP
     */
    public static Map<String, Object> LINKMAP = new LinkedHashMap<String, Object>() {

        private static final long serialVersionUID = 1L;

        {
            put(CHANGE.code, CHANGE.name);
            put(SHARE.code, SHARE.name);
            put(CANCEL.code, CANCEL.name);
            put(RECYCLE_C.code, RECYCLE_C.name);
            put(RECYCLE_S.code, RECYCLE_S.name);
            put(RECYCLE_A.code, RECYCLE_A.name);
        }
    };
}
