package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.controller.BaseController;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.entity.information.InformationFlowTheme;
import com.quantumtime.qc.service.IInformationFlowThemeService;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * .Description:废弃，用户圈子 & Created on 2019/11/12 11:00
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@Api(tags = "圈子管理")
@RestController
@RequestMapping("/informationFlowTheme")
@Slf4j
public class InformationFlowThemeController
        extends BaseController<InformationFlowTheme, Long, IInformationFlowThemeService> {

    @ApiOperation(
            value = "圈子列表 支持模糊查询 支持分页",
            notes = "分页查询 id为当前列表的最后记录id 如查询最新 则不传id size为取记录数 传入圈子名称filters.title 可为空")
    @PostMapping("/findThemeList")
    public Result findThemeList(@RequestBody BasePage<InformationFlowTheme, Long> page) {
        if (page == null || page.getSize() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "分页参数");
        }
        return Result.success(this.baseService.findThemeList(page));
    }

    @ApiOperation(value = "加入圈子", notes = "加入圈子 传圈子ID")
    @PostMapping("/join")
    public Result join(@RequestBody InformationFlowTheme theme) {
        return Result.success(this.baseService.joinTheme(theme.getId()));
    }

    @ApiOperation(value = "退出圈子", notes = "退出圈子 传圈子ID")
    @PostMapping("/unJoin")
    public Result unJoin(@RequestBody InformationFlowTheme theme) {
        return Result.success(this.baseService.unjoinTheme(theme.getId()));
    }
}
