package com.quantumtime.qc.controller;

import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.UserTaskService;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * .Description:本本跟新接口API & Created on 2019/11/12 11:12
 *
 * @author dong
 * @version 1.0
 */
@Api(tags = "用户任务")
@RestController
@RequestMapping("/task")
@Slf4j
public class UserTaskController {
    @Resource private UserTaskService userTaskService;
    @Resource private AccountHelp accountHelp;
    @ApiOperation(value = "可领取任务列表", notes = "")
    @GetMapping("/fetchTaskList")
    public Result fetchTaskList(@RequestParam(required = false) String province,
                                @RequestParam(required = false) String city) {
        if (log.isDebugEnabled()) {
            log.debug("fetchTaskList:" + province + "," + city);
        }
        try {
            if (province == null) {
                province = "";
            }
            if (city == null) {
                city = "";
            }
            return Result.success(userTaskService.fetchTaskList(province, city));
        } catch (Exception e) {
            log.error("fetchTaskList异常:", e);
            return Result.result(10700, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "领取任务", notes = "")
    @GetMapping("/fetchOne")
    public Result fetchOne(@RequestParam Long taskId) {
        if (log.isDebugEnabled()) {
            log.debug("fetchOne:" + taskId);
        }
        try {
            return Result.success(userTaskService.fetchOne(taskId));
        } catch (Exception e) {
            log.error("fetchOne异常:", e);
            return Result.result(10701, e.getMessage(), null, e.getMessage());
        }
    }
    @ApiOperation(value = "斗地主领取点赞任务", notes = "")
    @GetMapping("/clickTask")
    public Result clickTask(@RequestParam Long taskId, @RequestParam String uid) {
         Boolean receive =userTaskService.likeTaskStart(taskId,uid);
         if(receive==true){
             return Result.success(receive);
         }else{
             return Result.result(10701, "你已经领取过任务了", null, "null");
         }



    }
}
