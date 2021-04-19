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

import cn.vbill.middleware.porter.manager.core.entity.DataSource;
import cn.vbill.middleware.porter.manager.core.entity.DataSourcePlugin;
import cn.vbill.middleware.porter.manager.core.mapper.DataSourcePluginMapper;
import cn.vbill.middleware.porter.manager.service.DataSourcePluginService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据源信息关联表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
@Service
public class DataSourcePluginServiceImpl implements DataSourcePluginService {

    @Autowired
    private DataSourcePluginMapper dataSourcePluginMapper;

    @Override
    public Integer insert(DataSourcePlugin dataSourcePlugin) {
        return dataSourcePluginMapper.insert(dataSourcePlugin);
    }

    @Override
    public Integer update(Long id, DataSourcePlugin dataSourcePlugin) {
        return dataSourcePluginMapper.update(id, dataSourcePlugin);
    }

    @Override
    public Integer delete(Long id) {
        return dataSourcePluginMapper.delete(id);
    }

    @Override
    public DataSourcePlugin selectById(Long id) {
        return dataSourcePluginMapper.selectById(id);
    }

    @Override
    public Page<DataSourcePlugin> page(Page<DataSourcePlugin> page) {
        Integer total = dataSourcePluginMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataSourcePluginMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public void insertSelective(DataSource dataSource) {
        //获取 DataSource 新增成功的自增id
        for (DataSourcePlugin dataSourcePlugin : dataSource.getPlugins()) {
            dataSourcePlugin.setSourceId(dataSource.getId());
        }
        dataSourcePluginMapper.insertDataSourcePlugins(dataSource.getPlugins());
    }

    @Override
    public List<DataSourcePlugin> findListBySourceID(Long sourceId) {
        return dataSourcePluginMapper.findListBySourceID(sourceId);
    }
}
