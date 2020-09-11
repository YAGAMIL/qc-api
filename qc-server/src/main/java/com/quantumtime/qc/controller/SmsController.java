package com.quantumtime.qc.controller;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonResponse;
import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.constant.SmsConstant;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.common.service.ISmsService;
import com.quantumtime.qc.common.enums.UserStateEnum;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.service.IRedisService;
import com.quantumtime.qc.service.ISensorAnalytics;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.common.utils.VerifyUtil;
import com.quantumtime.qc.vo.Result;
import com.quantumtime.qc.vo.SmsResponseVo;
import com.quantumtime.qc.vo.SmsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信服务
 */
@Api(tags = "短信服务接口")
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private ISmsService smsService;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private IUserService userService;

    /**
     * smsRegisterCode: 'SMS_163625771'
     * smsLoginCode: 'SMS_163620885'
     * signName: '量子社区'
     */
    @Value("${aliyun.smsRegisterCode}")
    private String smsRegisterCode;
    @Value("${aliyun.smsLoginCode}")
    private String smsLoginCode;

    @Value("${aliyun.smsModifyPhoneCode}")
    private String smsModifyPhoneCode;

    @Value("${aliyun.signName}")
    private String signName;

    @Autowired
    private ISensorAnalytics sensorAnalytics;

    /**
     * 发送短信
     *
     * @param smsVo
     * @return
     */
    @PostMapping("/sendSms")
    @ApiOperation(value = "发送短信验证码", notes = "返回短信发送结果 Code为ok代表成功 其他为失败 action 0为注册验证码 1为登录验证码 2为修改手机号first 3为修改手机号second 4为绑定手机号")
    public Result genTLSSignatureEx(@RequestBody SmsVo smsVo) {
        CommonResponse commonResponse = null;
        if (VerifyUtil.notPhone(smsVo.getPhoneNumber())) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_PHONE_FORMAT_ERROR, new Throwable());
        }

        String captcha = smsService.generateCaptcha();
        Map<String, String> params = new HashMap<>();
        params.put("code", captcha);
        if (smsVo.getAction().equals(SmsConstant.SMS_REGISTER.toString())) {
            //校验最近55s是否发过短信
            if (redisService.exists(SmsConstant.SMS_REGISTER_LOCK_PREFIX + smsVo.getPhoneNumber())) {
                throw new BizException(ErrorCodeConstant.SMS_OVER_SEND, new Throwable(), "1");
            }
            //校验是否已经注册过
            if (userService.findByPhone(smsVo.getPhoneNumber()) != null) {
                throw new BizException(ErrorCodeConstant.ACCOUNT_PHONE_EXIST_REGISTER, new Throwable());
            }
            commonResponse = smsService.sendSms(smsVo.getPhoneNumber(), signName, smsRegisterCode, params);
            collect(smsVo, commonResponse);
            checkSmsResponse(commonResponse);
            redisService.set(SmsConstant.SMS_REGISTER_PREFIX + smsVo.getPhoneNumber(), captcha, 60 * 5L);
            redisService.set(SmsConstant.SMS_REGISTER_LOCK_PREFIX + smsVo.getPhoneNumber(), "", 55L);
            return Result.success(commonResponse);
        } else if (smsVo.getAction().equals(SmsConstant.SMS_LOGIN.toString())) {
            //校验最近50s是否发过短信
            if (redisService.exists(SmsConstant.SMS_LOGIN_LOCK_PREFIX + smsVo.getPhoneNumber())) {
                throw new BizException(ErrorCodeConstant.SMS_OVER_SEND, new Throwable(), "1");
            }
            //校验是否过了真实性验证
            User user = userService.findByPhone(smsVo.getPhoneNumber());
            if (user == null) {
                throw new BizException(ErrorCodeConstant.ACCOUNT_PHONE_NOT_EXIST_REGISTER, new Throwable());
            }
            if (UserStateEnum.getEnumByCode(user.getState()).equals(UserStateEnum.DISABLE)) {
                throw new BizException(ErrorCodeConstant.ACCOUNT_DISABLE, new Throwable());
            }
            commonResponse = smsService.sendSms(smsVo.getPhoneNumber(), signName, smsLoginCode, params);
            collect(smsVo, commonResponse);
            checkSmsResponse(commonResponse);
            redisService.set(SmsConstant.SMS_LOGIN_PREFIX + smsVo.getPhoneNumber(), captcha, 60 * 5L);
            redisService.set(SmsConstant.SMS_LOGIN_LOCK_PREFIX + smsVo.getPhoneNumber(), "", 55L);
            return Result.success(commonResponse);
        } else if (smsVo.getAction().equals(SmsConstant.SMS_MODIFY_PHONE_FIRST.toString())) {
            //校验最近50s是否发过短信
            if (redisService.exists(SmsConstant.SMS_MODIFY_PHONE_FIRST_LOCK_PREFIX + smsVo.getPhoneNumber())) {
                throw new BizException(ErrorCodeConstant.SMS_OVER_SEND, new Throwable(), "1");
            }
            commonResponse = smsService.sendSms(smsVo.getPhoneNumber(), signName, smsModifyPhoneCode, params);
            collect(smsVo, commonResponse);
            checkSmsResponse(commonResponse);
            redisService.set(SmsConstant.SMS_MODIFY_PHONE_FIRST_PREFIX + smsVo.getPhoneNumber(), captcha, 60 * 5L);
            redisService.set(SmsConstant.SMS_MODIFY_PHONE_FIRST_LOCK_PREFIX + smsVo.getPhoneNumber(), "", 55L);
            return Result.success(commonResponse);
        } else if (smsVo.getAction().equals(SmsConstant.SMS_MODIFY_PHONE_SECOND.toString())) {
            //校验最近50s是否发过短信
            if (redisService.exists(SmsConstant.SMS_MODIFY_PHONE_SECOND_LOCK_PREFIX + smsVo.getPhoneNumber())) {
                throw new BizException(ErrorCodeConstant.SMS_OVER_SEND, new Throwable(), "1");
            }
            //校验是否过了真实性验证
            User user = userService.findByPhone(smsVo.getPhoneNumber());
            if (user == null) {
                throw new BizException(ErrorCodeConstant.ACCOUNT_PHONE_EXIST_REGISTER, new Throwable());
            }
            if (UserStateEnum.getEnumByCode(user.getState()).equals(UserStateEnum.DISABLE)
                    || UserStateEnum.getEnumByCode(user.getState()).equals(UserStateEnum.DELETE)) {
                throw new BizException(ErrorCodeConstant.ACCOUNT_DISABLE, new Throwable());
            }
            commonResponse = smsService.sendSms(smsVo.getPhoneNumber(), signName, smsModifyPhoneCode, params);
            collect(smsVo, commonResponse);
            checkSmsResponse(commonResponse);
            redisService.set(SmsConstant.SMS_MODIFY_PHONE_SECOND_PREFIX + smsVo.getPhoneNumber(), captcha, 60 *5L);
            redisService.set(SmsConstant.SMS_MODIFY_PHONE_SECOND_LOCK_PREFIX + smsVo.getPhoneNumber(), "", 55L);
            return Result.success(commonResponse);
        } else if (smsVo.getAction().equals(SmsConstant.SMS_BINDING_4.toString())) {
            //校验最近50s是否发过短信
            if (redisService.exists(SmsConstant.SMS_MODIFY_PHONE_SECOND_LOCK_PREFIX + smsVo.getPhoneNumber())) {
                throw new BizException(ErrorCodeConstant.SMS_OVER_SEND, new Throwable(), "1");
            }
            commonResponse = smsService.sendSms(smsVo.getPhoneNumber(), signName, smsModifyPhoneCode, params);
            collect(smsVo, commonResponse);
            checkSmsResponse(commonResponse);
            redisService.set(SmsConstant.SMS_BIND_WECHAT_PREFIX + smsVo.getPhoneNumber(), captcha, 60 * 5L);
            redisService.set(SmsConstant.SMS_BIND_WECHAT_PREFIX_LOCK_PREFIX + smsVo.getPhoneNumber(), "", 55L);
            return Result.success(commonResponse);
        }
        throw new BizVerifyException(ErrorCodeConstant.SMS_ACTION_ERROR, new Throwable());

    }

    private void collect(SmsVo smsVo, CommonResponse commonResponse) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("service_type", smsVo.getAction());
        if (commonResponse == null || StringUtils.isEmpty(commonResponse.getData())) {
            properties.put("is_success", false);
            properties.put("fail_reason", ErrorCodeConstant.SMS_SEND_ERROR);
            sensorAnalytics.track(smsVo.getPhoneNumber(), false,"getCodeResult", properties);
            return;
        }
        SmsResponseVo smsResponseVo = JSON.parseObject(commonResponse.getData(), SmsResponseVo.class);
        if (smsResponseVo.getCode().equals(SmsConstant.SMS_RESPONSE_CODE_CONTROL)) {
            properties.put("is_success", false);
            properties.put("fail_reason", ErrorCodeConstant.SMS_CONTROL);
        } else if (!smsResponseVo.getCode().equals(SmsConstant.SMS_RESPONSE_CODE_OK)) {
            properties.put("is_success", false);
            properties.put("fail_reason", ErrorCodeConstant.SMS_SEND_ERROR);
        } else {
            properties.put("is_success", true);
            properties.put("fail_reason", "");
        }
        sensorAnalytics.track(smsVo.getPhoneNumber(), false,"getCodeResult", properties);
    }

    private void checkSmsResponse(CommonResponse commonResponse) {
        if (commonResponse == null || StringUtils.isEmpty(commonResponse.getData())) {
            throw new BizException(ErrorCodeConstant.SMS_SEND_ERROR, new Throwable());
        }
        SmsResponseVo smsResponseVo = JSON.parseObject(commonResponse.getData(), SmsResponseVo.class);
        if (smsResponseVo.getCode().equals(SmsConstant.SMS_RESPONSE_CODE_CONTROL)) {
            throw new BizException(ErrorCodeConstant.SMS_CONTROL, new Throwable());
        } else if (!smsResponseVo.getCode().equals(SmsConstant.SMS_RESPONSE_CODE_OK)) {
            throw new BizException(ErrorCodeConstant.SMS_SEND_ERROR, new Throwable());
        }
    }


}
