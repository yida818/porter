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
package cn.vbill.middleware.porter.plugin.consumer.canal;

import lombok.Getter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月19日 11:22
 * @version: V1.0
 * @review: zkevin/2019年03月19日 11:22
 */
public enum CanalConsumerConst {
    CONSUMER_SOURCE_TYPE_NAME("CANAL"),
    CONSUMER_PLUGIN_NAME("CanalFetch");
    @Getter
    private String code;
    CanalConsumerConst(String code) {
        this.code = code;
    }
}
