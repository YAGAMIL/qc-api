package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.duiba.BulidUrl;
import com.quantumtime.qc.common.duiba.CreditTool;
import com.quantumtime.qc.common.duiba.entity.CreditConsumeParams;
import com.quantumtime.qc.common.duiba.entity.CreditNotifyParams;
import com.quantumtime.qc.common.duiba.result.CreditConsumeResult;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.DuiBaService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.vo.DuiBaVo;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

import static com.quantumtime.qc.common.constant.ErrorCodeConstant.ACCOUNT_TOKEN_ERROR;

/**
 * Description:兑吧urk接口 Program:qc-api Created on 2019-12-06 14:39
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Api(tags = "兑吧接口")
@RestController
@RequestMapping("/duiba")
@Slf4j
public class DuiBaController {
    @Resource
    private AccountHelp accountHelp;
    @Resource
    private IUserService userService;
    @Resource
    private DuiBaService duiBaService;

    @ApiOperation(value = "获取免登录URL", notes = "兑吧免登录url")
    @GetMapping("/loginUrl")
    public Result loginFreeUrl() {
        User user = Optional.ofNullable(accountHelp.getCurrentUser())
                .orElseThrow(() -> BizException.throwException(ACCOUNT_TOKEN_ERROR));
        user = userService.findById(user.getUid());
        return Result.success(BulidUrl.buildAutoLoginRequest(user.getUid(), user.getScore(), null));
    }

    @ApiOperation(value = "兑换通知接口", notes = "兑吧免登录url")
    @GetMapping("/exchange/notice")
    public CreditConsumeResult exchangeNotice(
            boolean success,
            String errorMessage,
            Long bizId,
            String sign,
            String orderNum,
            String appKey,
            Long timestamp) {
        return new CreditConsumeResult(duiBaService.verifyResults(new CreditNotifyParams()
                .setSuccess(success)
                .setAppKey(appKey)
                .setBizId(String.valueOf(bizId))
                .setTimestamp(new Date(timestamp))
                .setErrorMessage(errorMessage)
                .setOrderNum(orderNum)));
    }

    @ApiOperation(value = "兌吧积分消费接口", notes = "兌吧积分消费接口")
    @GetMapping("/exchange/consume")
    public String consume(String appKey,
                          Long credits,
                          String orderNum,
                          String description,
                          String type,
                          Integer facePrice,
                          Integer actualPrice,
                          String uid,
                          boolean waitAudit,
                          String ip,
                          String params,
                          String transfer) {
        CreditConsumeParams creditConsumeParams = new CreditConsumeParams();
        creditConsumeParams.setAppKey(appKey);
        creditConsumeParams.setCredits(credits);
        creditConsumeParams.setOrderNum(orderNum);
        creditConsumeParams.setDescription(description);
        creditConsumeParams.setType(type);
        creditConsumeParams.setFacePrice(facePrice);
        creditConsumeParams.setActualPrice(actualPrice);
        creditConsumeParams.setUid(uid);
        creditConsumeParams.setWaitAudit(waitAudit);
        creditConsumeParams.setTransfer(transfer);
        String bizId =null;
        boolean success = false;
        Long surplusCredit=0L;
        String errorMessage = "扣除积分失败";
        try {
            DuiBaVo duiBaVo = duiBaService.integralUpdate(creditConsumeParams);
             bizId = String.valueOf(duiBaVo.getBizId()); //开发者业务订单号，保证唯一不重复
            surplusCredit = duiBaVo.getSurplus(); // getCredits()是根据开发者自身业务，获取的用户最新剩余积分数。
            success = true;
        } catch (Exception e) {
            success = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        }
        CreditConsumeResult ccr = new CreditConsumeResult(success);
        ccr.setBizId(bizId);
        ccr.setErrorMessage(errorMessage);
        ccr.setCredits(surplusCredit);
        return ccr.toString();//返回扣积分结果json信息
    }




}
