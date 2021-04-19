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
package cn.vbill.middleware.porter.manager.controller;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.vbill.middleware.porter.manager.core.dto.DataAuthorityVo;
import cn.vbill.middleware.porter.manager.core.enums.DataSignEnum;
import cn.vbill.middleware.porter.manager.service.DataAuthorityService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 数据权限控制表 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-03-28 15:21:58
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-28 15:21:58
 */
@Api(description = "数据权限控制表管理")
@RestController
@RequestMapping("/manager/dataauthority")
public class DataAuthorityController {

    @Autowired
    protected DataAuthorityService dataAuthorityService;

    /**
     * 权限管理页面
     * 
     * @return
     */
    @PostMapping("/dataauthorityvo")
    @ApiOperation(value = "权限页面数据vo", notes = "权限页面数据vo")
    public ResponseMessage dataAuthorityVo(@RequestParam(value = "dataSignEnum", required = true) DataSignEnum sign,
            @RequestParam(value = "objectId", required = true) Long objectId) {
        DataAuthorityVo vo = dataAuthorityService.dataAuthorityVo(sign.getTable(), objectId);
        return ok(vo);
    }

    /**
     * 移交权限
     * 
     * @return
     */
    @PostMapping("/turnover")
    @ApiOperation(value = "移交权限", notes = "移交权限")
    public ResponseMessage turnover(@RequestParam(value = "dataSignEnum", required = true) DataSignEnum sign,
            @RequestParam(value = "objectId", required = true) Long objectId,
            @RequestParam(value = "ownerId", required = true) Long ownerId) {

        Boolean key = dataAuthorityService.turnover(sign.getTable(), objectId, ownerId);
        return key ? ok() : ResponseMessage.error("移交数据所有人失败，请联系管理员！");
    }

    /**
     * add共享权限
     * 
     * @return
     */
    @PostMapping("/addshare")
    @ApiOperation(value = "add共享权限", notes = "add共享权限")
    public ResponseMessage addShare(@RequestParam(value = "dataSignEnum", required = true) DataSignEnum sign,
            @RequestParam(value = "objectId", required = true) Long objectId,
            @RequestParam(value = "ownerId", required = true) Long ownerId) {
        Boolean key = dataAuthorityService.addShare(sign.getTable(), objectId, ownerId);
        return key ? ok() : ResponseMessage.error("添加数据共享人失败，请联系管理员！");
    }

    /**
     * del共享权限
     * 
     * @return
     */
    @DeleteMapping("/delshare")
    @ApiOperation(value = "del共享权限", notes = "del共享权限")
    public ResponseMessage delShare(@RequestParam(value = "dataSignEnum", required = true) DataSignEnum sign,
            @RequestParam(value = "objectId", required = true) Long objectId,
            @RequestParam(value = "ownerId", required = true) Long ownerId) {
        Boolean key = dataAuthorityService.delShare(sign.getTable(), objectId, ownerId);
        return key ? ok() : ResponseMessage.error("删除数据共享人失败，请联系管理员！");
    }

    /**
     * 共享权限
     * 
     * @return
     */
    @PostMapping("/share")
    @ApiOperation(value = "共享权限", notes = "共享权限")
    public ResponseMessage share(@RequestParam(value = "dataSignEnum", required = true) DataSignEnum sign,
            @RequestParam(value = "objectId", required = true) Long objectId,
            @RequestParam(value = "ownerIds", required = true) Long[] ownerIds) {
        Boolean key = dataAuthorityService.share(sign.getTable(), objectId, ownerIds);
        return key ? ok() : ResponseMessage.error("数据共享人更新失败，请联系管理员！");
    }

    /**
     * 放弃权限
     * 
     * @return
     */
    @PostMapping("/waive")
    @ApiOperation(value = "放弃权限", notes = "放弃权限")
    public ResponseMessage waive(@RequestParam(value = "dataSignEnum", required = true) DataSignEnum sign,
            @RequestParam(value = "objectId", required = true) Long objectId) {
        Boolean key = dataAuthorityService.waive(sign.getTable(), objectId);
        return key ? ok() : ResponseMessage.error("放弃数据权限失败，请联系管理员！");
    }
}
