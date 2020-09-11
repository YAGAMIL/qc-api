package com.quantumtime.qc.controller;

import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.vo.QualifyVo;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .Description:身份认证 & Created on 2019/11/12 11:04
 *
 * @author Gan
 * @version 1.0
 */
@Api(tags = "身份认证")
@RestController
@RequestMapping("/qualify")
@Slf4j
public class QualifyController {

    @Resource private IUserService userService;

    @ApiOperation(value = "发起身份认证", notes = "返回是否发起成功")
    @PostMapping("/request")
    public Result request(@RequestBody QualifyVo qualifyVo) {
        Assert.notNull(qualifyVo.getNickname(), "昵称为空");
        Assert.notNull(qualifyVo.getPhone(), "电话为空");
        Assert.notNull(qualifyVo.getHobbies(), "特长爱好为空");

        userService.qualify(qualifyVo);
        return Result.success("认证发起成功");
    }

    @ApiOperation(value = "身份认证回调", notes = "返回身份认证信息")
    @PostMapping("/callback")
    public Result callback(@RequestBody QualifyVo qualifyVo) {
        Assert.notNull(qualifyVo.getUid(), "uid为空");
        Assert.notNull(qualifyVo.getNickname(), "昵称为空");
        Assert.notNull(qualifyVo.getPhone(), "电话为空");
        Assert.notNull(qualifyVo.getHobbies(), "特长爱好为空");

        userService.qualifyCallback(qualifyVo);
        return Result.success("身份认证回调成功");
    }
}
