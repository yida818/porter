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

package cn.vbill.middleware.porter.manager.web.tl;


import cn.vbill.middleware.porter.manager.web.token.Token;
import cn.vbill.middleware.porter.manager.web.token.TokenUtil;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class WebToeknContext {

    private static ThreadLocal<String> TOKENHODLER = new ThreadLocal<String>();

    /**
     * initToken
     *
     * @date 2018/8/9 下午3:37
     * @param: [tokenId]
     * @return: void
     */
    public static void initToken(String tokenId) {
        TOKENHODLER.set(tokenId);
    }

    public static ThreadLocal<String> getTokenHodler() {
        return TOKENHODLER;
    }

    public static void setTokenHodler(ThreadLocal<String> tokenHodler) {
        WebToeknContext.TOKENHODLER = tokenHodler;
    }

    /**
     * getToken
     *
     * @date 2018/8/9 下午3:37
     * @param: [classT]
     * @return: T
     */
    public static <T extends Token> T getToken(Class<T> classT) throws Exception {
        return TokenUtil.unsign(TOKENHODLER.get(), classT);
    }
}
