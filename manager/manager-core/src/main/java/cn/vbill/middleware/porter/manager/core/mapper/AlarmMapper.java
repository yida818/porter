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

package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.Alarm;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 告警配置表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public interface AlarmMapper {

    /**
     * selectFinallyOne
     *
     * @date 2018/8/9 下午5:30
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.core.event.Alarm
     */
    Alarm selectFinallyOne();

    /**
     * 新增
     *
     * @param alarm
     */
    Integer insert(Alarm alarm);

    /**
     * 修改
     *
     * @param alarm
     */
    Integer update(@Param("id") Long id, @Param("alarm") Alarm alarm);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    Alarm selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<Alarm> page(@Param("page") Page<Alarm> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 验证新增
     *
     * @param alarm
     * @return
     */
    Integer insertSelective(Alarm alarm);

    /**
     * 验证修改
     *
     * @param id
     * @param alarm
     * @return
     */
    Integer updateSelective(@Param("id") Long id, @Param("alarm") Alarm alarm);
}