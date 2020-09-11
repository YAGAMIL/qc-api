package com.quantumtime.qc.controller;

import com.quantumtime.qc.service.ConfigService;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * .Description:本本跟新接口API & Created on 2019/11/12 11:12
 *
 * @author dong
 * @version 1.0
 */
@Api(tags = "配置接口")
@RestController
@RequestMapping("/config")
@Slf4j
public class ConfigController {
    @Resource private ConfigService configService;

    @ApiOperation(value = "所有配置")
    @GetMapping("/all")
    public Result all() {
        return Result.success("success", configService.all());
    }
}
