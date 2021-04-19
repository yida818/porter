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

package cn.vbill.middleware.porter.boot.config;

import cn.vbill.middleware.porter.common.warning.config.WarningConfig;
import cn.vbill.middleware.porter.common.cluster.config.ClusterConfig;
import cn.vbill.middleware.porter.common.config.StatisticConfig;
import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 10:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 10:14
 */
@ConfigurationProperties(prefix = "porter")
@Setter
@Getter
@Component
public class NodeConfig {
    private String id = UUID.randomUUID().toString();
    private Integer workLimit = 10;
    private StatisticConfig statistic = new StatisticConfig();
    private WarningConfig alert;
    private ClusterConfig cluster;
    private List<TaskConfig> task = new ArrayList<>();

    //允许默认定时GC
    private boolean gc = false;
    private Integer gcDelayOfMinutes = 30;
}

