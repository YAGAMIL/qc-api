package com.quantumtime.qc.controller;

import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.UserRepository;
import com.quantumtime.qc.service.RelationshipService;
import com.quantumtime.qc.service.WithdrawService;
import com.quantumtime.qc.service.impl.RelationshipServiceImpl;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description:积分服务接口 Program:qc-api Created on 2019-12-03 21:17
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Api(tags = "积分服务接口")
@RestController
@RequestMapping("/integral")
public class IntegralController {

    @Resource
    private UserRepository userRepository;
    @Resource
    private WithdrawService withdrawService;
    @Resource
    private RelationshipService relationshipService;
    @Resource
    private AccountHelp accountHelp;
    @Resource
    RelationshipServiceImpl relationshipServiceimpl;

    private static final int IS_RECEIVE_FALSE = 0;

    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "提现", notes = "提现接口")
    @PutMapping("/withdraw/{consumeScore}/{type}")
    public Result squareFeeds(
            @PathVariable("consumeScore") Long consumeScore, @PathVariable("type") Integer type) {
        return Result.success(
                withdrawService.cash(accountHelp.getCurrentUser().getUid(), consumeScore, type));
    }

    @ApiOperation(value = "邀请界面信息接口", notes = "返回我的积分数，是否可以填邀请码，以及我自己的邀请码")
    @GetMapping("/integralDetail")
    public Result integralDetail(@RequestParam("uid") String uid) {
        return Result.success(relationshipService.integralDetail(uid));
    }

    @SuppressWarnings("InvitationUser")
    @ApiOperation(value = "建立邀请关系", notes = "填写邀请码接口")
    @PutMapping("/InvitationUser/{inviterId}/{inviteeId}")
    public Result invitationUser(@PathVariable("inviterId") Integer inviterId, @PathVariable("inviteeId") Integer inviteeId) {
        int relation = relationshipServiceimpl.isInvite(inviteeId);
        User user = userRepository.findById(inviterId);
        if (relation == IS_RECEIVE_FALSE) {
            return Result.result(11000, "你已经填写过邀请码了", null, "你已经填写过邀请码了");
        } else if (user == null ||inviterId==inviteeId) {
            return Result.result(11001, "该邀请码不正确", null, "该邀请码不正确");
        } else {
            return Result.success(relationshipService.invitationUser(inviterId, inviteeId));
        }


    }

    @ApiOperation(value = "我的积分记录", notes = "返回我的邀请用用户信息的list")
    @GetMapping("/myInviteUseList")
    public Result myInviteUseList(
            @RequestParam("id") Integer id,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize) {
        Assert.state(pageNumber > 0, "pageNum必须大于0");
        Assert.state(pageSize > 0, "pageSize必须大于0");
        return Result.success(relationshipService.myInviteUseList(id, pageNumber, pageSize));
    }

    @ApiOperation(value = "我的提现记录", notes = "返回我的提现记录list")
    @GetMapping("/myIntegral/{uid}/{pageNumber}/{pageSize}")
    public Result myIntegralList(
            @PathVariable("uid") String uid,
            @PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize) {
        Assert.state(pageNumber > 0, "pageNum必须大于0");
        Assert.state(pageSize > 0, "pageSize必须大于0");
        return Result.success(relationshipService.myIntegralList(uid, pageNumber, pageSize));
    }

    @ApiOperation(value = "我的提现记录", notes = "返回我的提现记录list")
    @GetMapping("/myWithdrawList")
    public Result myWithdrawList(
            @RequestParam("uid") String uid,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize) {
        Assert.state(pageNumber > 0, "pageNum必须大于0");
        Assert.state(pageSize > 0, "pageSize必须大于0");
        return Result.success(relationshipService.myWithdrawList(uid, pageNumber, pageSize));
    }

    @ApiOperation(value = "我的积分记录", notes = "返回我的积分记录list")
    @GetMapping("/myScoreLogList")
    public Result myScoreLogList(
            @RequestParam("uid") String uid,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize) {
        Assert.state(pageNumber > 0, "pageNum必须大于0");
        Assert.state(pageSize > 0, "pageSize必须大于0");
        return Result.success(relationshipService.myScoreLogList(uid, pageNumber, pageSize));
    }

    @ApiOperation(value = "我的兑换记录", notes = "返回我的兑换记录list")
    @GetMapping("/exchangeLogs/{uid}/{pageNumber}/{pageSize}")
    public Result myExchangeList(
            @PathVariable("uid") String uid,
            @PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize) {
        Assert.state(pageNumber > 0, "pageNum必须大于0");
        Assert.state(pageSize > 0, "pageSize必须大于0");
        return Result.success(relationshipService.myExchangeList(uid, pageNumber, pageSize));
    }

    @ApiOperation(value = "斗地主点赞任务领取金积分", notes = "返回Boolean")
    @GetMapping("/exchangeLogs/{uid}/{activityId}")
    public Result clickTaskReceive(@PathVariable("uid") String uid,
                                   @PathVariable("activityId") Long activityId) {
         Boolean isReceive= relationshipService.clickTaskReceive(uid,activityId);
        if(isReceive==true){
            return Result.success(isReceive);
        }else{
            return Result.result(0,"您还没有完成该任务呦",false, "领取任务异常");
        }
    }

    @ApiOperation(value = "发视频领任务积分", notes = "返回Boolean")
    @GetMapping("/act/{uid}/{activityId}")
    public Result sendVideoTask(@PathVariable("uid") String uid,
                                   @PathVariable("activityId") Long activityId) {
        Boolean isReceive= relationshipService.sendVideoReceive(uid,activityId);
        if(isReceive==true){
            return Result.success(isReceive);
        }else{
            return Result.result(0,"您还没有完成该任务呦",false, "领取任务异常");
        }
    }

 /*   @ApiOperation(value = "发布斗地主视频任务领取金积分", notes = "返回Boolean")
    @GetMapping("/exchangeLogs/{uid}/{activityId}")
    public Result videoTaskReceive(@PathVariable("uid") String uid,
                                   @PathVariable("activityId") Long activityId) {
        Boolean isReceive= relationshipService.videoTaskReceive(uid,activityId);
        if(isReceive){
            return Result.success(isReceive);
        }else{
            return Result.result(0,"您还没有完成该任务呦",false, "领取任务异常");
        }
    }*/
}
