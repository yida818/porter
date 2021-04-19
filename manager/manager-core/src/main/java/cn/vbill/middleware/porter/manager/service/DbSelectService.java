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

package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.dto.JDBCVo;
import cn.vbill.middleware.porter.manager.core.entity.DataSource;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public interface DbSelectService {

    /**
     * List
     *
     * @date 2018/8/10 上午11:25
     * @param: [dataSource, jvo, sql, map]
     * @return: java.util.List<java.lang.String>
     */
    List<String> list(DataSource dataSource, JDBCVo jvo, String sql, Map<String, Object> map);

    /**
     * pageTotal
     *
     * @date 2018/8/10 上午11:26
     * @param: [dataSource, jvo, sql, prefix, tableName]
     * @return: java.lang.Long
     */
    Long pageTotal(DataSource dataSource, JDBCVo jvo, String sql, String prefix, String tableName);

    /**
     * page
     *
     * @date 2018/8/10 上午11:26
     * @param: [dataSource, jvo, page, sql, prefix, tableName]
     * @return: java.util.List<java.lang.Object>
     */
    List<Object> page(DataSource dataSource, JDBCVo jvo, Page<Object> page, String sql, String prefix, String tableName);

    /**
     * fieldList
     *
     * @date 2018/8/10 上午11:26
     * @param: [dataSource, jvo, sql, tableAllName]
     * @return: java.util.List<java.lang.String>
     */
    List<String> fieldList(DataSource dataSource, JDBCVo jvo, String sql, String tableAllName);
}
