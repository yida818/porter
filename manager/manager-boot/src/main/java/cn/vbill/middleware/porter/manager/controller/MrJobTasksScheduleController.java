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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.vbill.middleware.porter.manager.core.entity.MrJobTasksSchedule;
import cn.vbill.middleware.porter.manager.service.MrJobTasksScheduleService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 任务泳道进度表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "任务泳道进度表管理")
@RestController
@RequestMapping("/manager/mrjobtasksschedule")
public class MrJobTasksScheduleController {

    @Autowired
    protected MrJobTasksScheduleService mrJobTasksScheduleService;

    /**
     * 根据jobId获取任务泳道id
     *
     * @author FuZizheng
     * @date 2018/4/3 下午4:06
     * @param: [jobId]
     * @return: ResponseMessage
     */
    @GetMapping("/getswimlane/{jobId}")
    @ApiOperation(value = "根据jobId获取任务泳道id", notes = "根据jobId获取任务泳道id")
    public ResponseMessage selectSwimlane(@PathVariable("jobId") String jobId) {
        List<MrJobTasksSchedule> list = mrJobTasksScheduleService.selectSwimlaneByJobId(jobId);
        return ok(list);
    }

    /**
     * 条件查询获取列表
     *
     * @author FuZizheng
     * @date 2018/4/4 下午2:49
     * @param: [jobId,
     *             heartBeatBeginDate, heartBeatEndDate]
     * @return: ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "jobId", required = false) String jobId,
            @RequestParam(value = "heartBeatBeginDate", required = false) String heartBeatBeginDate,
            @RequestParam(value = "heartBeatEndDate", required = false) String heartBeatEndDate) {
        List<MrJobTasksSchedule> list = mrJobTasksScheduleService.list(jobId, heartBeatBeginDate, heartBeatEndDate);
        return ok(list);
    }

    /**
     * 条件查询获取列表
     *
     * @param jobId
     * @param heartBeatBeginDate
     * @param heartBeatEndDate
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage listJobTasks(@RequestParam(value = "jobId", required = false) String jobId,
                                        @RequestParam(value = "heartBeatBeginDate", required = false) String heartBeatBeginDate,
                                        @RequestParam(value = "heartBeatEndDate", required = false) String heartBeatEndDate) {
        List<MrJobTasksSchedule> list = mrJobTasksScheduleService.listJobTasks(jobId, heartBeatBeginDate, heartBeatEndDate);
        return ok(list);
    }
}