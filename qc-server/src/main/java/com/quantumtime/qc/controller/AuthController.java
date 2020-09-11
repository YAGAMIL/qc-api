package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.constant.SmsConstant;
import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.common.utils.VerifyUtil;
import com.quantumtime.qc.service.IRedisService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.vo.RegisterUser;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Description: 注册（准备作废） Created on 2019/10/08 17:22
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@Slf4j
@Api(tags = "注册与登录")
@RestController
public class AuthController {

    @Resource private IUserService userService;

    @Resource private IRedisService redisService;

    @ApiOperation(value = "注册第一步", notes = "仅做验证码校验 phoneNumber 和verification")
    @PostMapping(value = "/auth/registerFirst")
    public Result registerFirst(@RequestBody RegisterUser registerUser) {
        if (VerifyUtil.notPhone(registerUser.getPhone())) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_PHONE_FORMAT_ERROR, new Throwable());
        }
        log.error(registerUser.getPhone());
        //noinspection unchecked
        if (!redisService.exists(SmsConstant.SMS_REGISTER_PREFIX + registerUser.getPhone())) {
            throw new BizException(ErrorCodeConstant.SMS_VERIFICATION_MISS, new Throwable());
        } else {
            //noinspection unchecked
            String verification =
                    redisService.get(SmsConstant.SMS_REGISTER_PREFIX + registerUser.getPhone()).toString();
            if (verification != null && verification.equals(registerUser.getVerification())) {
                return Result.success(true);
            }
        }
        throw new BizException(ErrorCodeConstant.SMS_VERIFICATION_ERROR, new Throwable());
    }

    @ApiOperation(value = "注册第二步", notes = "需带上验证码 除username其余都不能为空 返回密码 再根据密码登录 也可以直接根据token访问")
    @PostMapping(value = "/auth/old/registerSecond")
    public Result registerSecond(@RequestBody RegisterUser registerUser) {
        if (VerifyUtil.notPhone(registerUser.getPhone())) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_PHONE_FORMAT_ERROR, new Throwable());
        }
        if (StringUtils.isEmpty(registerUser.getVerification())) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "验证码或地址");
        }
        //noinspection unchecked
        if (!redisService.exists(SmsConstant.SMS_REGISTER_PREFIX + registerUser.getPhone())) {
            throw new BizException(ErrorCodeConstant.SMS_VERIFICATION_MISS, new Throwable());
        } else {
            return Result.success(userService.registerEnd(registerUser));
        }
    }
}
