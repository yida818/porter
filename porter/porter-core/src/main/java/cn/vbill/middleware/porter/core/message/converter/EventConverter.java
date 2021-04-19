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

package cn.vbill.middleware.porter.core.message.converter;

import cn.vbill.middleware.porter.core.message.MessageEvent;

import java.util.List;

/**
 * 消费器消息转换器
 */
public interface EventConverter {

    /**
     * 获取名称
     *
     * @date 2018/8/8 下午5:57
     * @param: []
     * @return: java.lang.String
     */
    String getName();

    /**
     * convert
     *
     * @date 2018/8/8 下午5:57
     * @param: [params]
     * @return: cn.vbill.middleware.porter.core.message.MessageEvent
     */
    default MessageEvent convert(Object... params) {
        return null;
    }

    /**
     * convertList
     *
     * @date 2018/8/8 下午5:57
     * @param: [params]
     * @return: java.util.List<cn.vbill.middleware.porter.core.message.MessageEvent>
     */
    default List<MessageEvent> convertList(Object... params) {
        return null;
    }
}
