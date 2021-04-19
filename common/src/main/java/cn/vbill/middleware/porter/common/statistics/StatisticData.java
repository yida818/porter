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

package cn.vbill.middleware.porter.common.statistics;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * 统计信息
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 16:37
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 16:37
 */

public class StatisticData {
    //丢弃
    @JSONField(serialize = false, deserialize = false)
    public static final String NAME = "discard";
    @JSONField(serialize = false, deserialize = false)
    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StatisticData.class);
    @JSONField(serialize = false, deserialize = false)
    private final FastDateFormat idDateFormat = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
    // 节点ID
    @Setter
    @Getter
    private String nodeId;
    @Getter @Setter private String category;
    public StatisticData() {
        category = NAME;
    }
    /**
     * 需要子类继承
     * @return
     */
    @JSONField(serialize = false, deserialize = false)
    protected  String getSubId() {
        return UUID.randomUUID().toString();
    }

    /**
     * StringBuilder
     * @return
     */
    @JSONField(serialize = false, deserialize = false)
    public String getId() {
        return new StringBuilder(getKey()).append("-").append(idDateFormat.format(new Date())).toString();
    }

    @JSONField(serialize = false, deserialize = false)
    public String getKey() {
        return new StringBuilder(nodeId).append("-").append(getSubId()).toString();
    }
    /**
     * toString
     * @return
     */
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * toPrintln
     * @return
     */
    public String toPrintln() {
        JSONObject jsonObject = JSONObject.parseObject(toString());
        StringBuilder sb = new StringBuilder();
        jsonObject.entrySet().forEach(p -> {
            sb.append(p.getKey()).append(":").append("      ").append((null != p.getValue() ? p.getValue() : ""))
                    .append(System.lineSeparator());
        });
        return sb.toString();
    }
}
