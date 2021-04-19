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

package cn.vbill.middleware.porter.common.cluster.config;

import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.dic.SourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class ZookeeperConfig extends SourceConfig {
    @Setter @Getter private String url;
    @Setter @Getter private int sessionTimeout = 1000 * 60 * 10;
    @Setter @Getter private int spinningTime = sessionTimeout;
    @Setter @Getter private int spinningPeer = 200;

    public  ZookeeperConfig() {
        sourceType = SourceType.ZOOKEEPER;
    }

    public ZookeeperConfig(Map<String, String> properties) {
        this();
        super.setProperties(properties);
    }

    @Override
    protected void childStuff() {

    }

    @Override
    protected String[] childStuffColumns() {
        return new String[0];
    }

    @Override
    protected boolean doCheck() {
        return true;
    }
}
