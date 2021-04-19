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

package cn.vbill.middleware.porter.common.cluster.dic;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 集群方案
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 11:42
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ClusterPlugin {

    /**
     * ZOOKEEPER
     */
    ZOOKEEPER("ZOOKEEPER", "zookeeper"),

    /**
     * STANDALONE
     */
    STANDALONE("STANDALONE", "standalone");

    /**
     * LINKMAP
     */
    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<>();

    static {
        LINKMAP.put(ZOOKEEPER.code, ZOOKEEPER.name);
        LINKMAP.put(STANDALONE.code, STANDALONE.name);
    }

    @Getter
    private final String code;
    @Getter
    private final String name;

}
