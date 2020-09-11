package com.quantumtime.qc.controller;

import com.quantumtime.qc.service.VersionService;
import com.quantumtime.qc.vo.Result;
import com.quantumtime.qc.wrap.VersionWarp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .Description:本本跟新接口API & Created on 2019/11/12 11:12
 *
 * @author dong
 * @version 1.0
 */
@Api(tags = "版本更新，")
@RestController
@RequestMapping("/version")
@Slf4j
public class VersionController {
    @Resource private VersionService versionService;

    @ApiOperation(value = "安卓版本更新", notes = "versionWarp不能为空")
    @PostMapping("/androidVersion")
    public Result versionUpdate(@RequestBody VersionWarp versionWarp) {
        return Result.success(versionService.updateVersion(versionWarp));
    }

    @ApiOperation(value = "获取最新版本下载的url")
    @GetMapping("/downloadUrl")
    public Result downloadUrl() {
        return Result.success("success", versionService.downloadUrl());
    }
}
