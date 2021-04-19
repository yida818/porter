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

package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.service.AlarmUserService;
import cn.vbill.middleware.porter.manager.service.CUserService;
import cn.vbill.middleware.porter.manager.core.entity.Alarm;
import cn.vbill.middleware.porter.manager.core.mapper.AlarmMapper;
import cn.vbill.middleware.porter.manager.service.AlarmPluginService;
import cn.vbill.middleware.porter.manager.service.AlarmService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 告警配置表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private AlarmPluginService alarmPluginService;

    @Autowired
    private AlarmUserService alarmUserService;

    @Autowired
    private CUserService cuserService;

    @Override
    public Alarm selectFinallyOne() {
        Alarm alarm = alarmMapper.selectFinallyOne();
        if (alarm != null && alarm.getId() != null) {
            alarm.setAlarmPlugins(alarmPluginService.selectByAlarmId(alarm.getId()));
            alarm.setAlarmUsers(alarmUserService.selectByAlarmId(alarm.getId()));
            return alarm;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Integer insert(Alarm alarm) {
        Integer i = alarmMapper.insert(alarm);
        alarmPluginService.insert(alarm);
        alarmUserService.insert(alarm);
        return i;
    }

    @Override
    public Integer update(Long id, Alarm alarm) {
        return alarmMapper.update(id, alarm);
    }

    @Override
    public Integer delete(Long id) {
        return alarmMapper.delete(id);
    }

    @Override
    public Alarm selectById(Long id) {
        Alarm alarm = alarmMapper.selectById(id);
        alarm.setAlarmPlugins(alarmPluginService.selectByAlarmId(id));
        alarm.setCusers(cuserService.selectByAlarmId(id));
        return alarm;
    }

    @Override
    public Page<Alarm> page(Page<Alarm> page) {
        Integer total = alarmMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(alarmMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public Integer insertSelective(Alarm alarm) {
        return alarmMapper.insertSelective(alarm);
    }

    @Override
    public Integer updateSelective(Long id, Alarm alarm) {
        return alarmMapper.updateSelective(id, alarm);
    }
}
