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

import cn.vbill.middleware.porter.manager.core.entity.CRole;

import java.util.List;

/**
 * 角色表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface CRoleMapper {

    /**
     * 查询所有
     *
     * @date 2018/8/9 下午6:14
     * @param: []
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.event.CRole>
     */
    List<CRole> findAll();

    /**
     * 查询类表
     *
     * @date 2018/8/9 下午6:15
     * @param: []
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.event.CRole>
     */
    List<CRole> findList();

    /**
     * 获取所有的权限(除了超级管理员)
     *
     * @return
     */
    List<CRole> getAll();
}