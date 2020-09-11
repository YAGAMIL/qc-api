package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.controller.BaseController;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.entity.information.InformationFlow;
import com.quantumtime.qc.service.IInformationFlowService;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * .Description:信息流管理 & Created on 2019/11/12 10:56
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@Api(tags = "信息流管理")
@RestController
@RequestMapping("/informationFlow")
@Slf4j
public class InformationFlowController extends BaseController<InformationFlow, Long, IInformationFlowService> {

    @ApiOperation(value = "发现与圈子", notes = "分页查询 id为当前列表的最后记录id 如查询最新 则不传id size为取记录数 如果查询为圈子需要传入filters.themeId")
    @PostMapping("/findPage")
    public Result findPage(@RequestBody BasePage<InformationFlow, Long> page) {
        if (page == null || page.getSize() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "分页参数");
        }
        return Result.success(baseService.findPage(page));
    }

    @ApiOperation(
            value = "转发列表",
            notes = "分页查询 id为当前列表的最后记录id 如查询最新 则不传id size为取记录数 filters.forwardHierarchy为当前信息流id 不能为空")
    @PostMapping("/findForwardList")
    public Result findForwardList(@RequestBody BasePage<InformationFlow, Long> page) {
        if (page == null
                || page.getSize() == null
                || page.getFilters() == null
                || StringUtils.isEmpty(page.getFilters().getForwardHierarchy())) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "参数");
        }
        return Result.success(baseService.findForwardList(page));
    }

    @ApiOperation(
            value = "发布信息流——包含圈子",
            notes =
                    "信息流content 内容 resourceUrls 图片或视频地址 多个用逗号分隔 如果是普通信息流 带上scope 0:所有人可见 1:小区验证用户可见 如果是圈子信息流 带上themeId 如果是转发 带上forwardHierarchy即上级id")
    @PostMapping("/publishInformationFlow")
    public Result publishInformationFlow(@RequestBody InformationFlow flow) {
        if (StringUtils.isEmpty(flow.getContent()) && StringUtils.isEmpty(flow.getForwardHierarchy())) {
            throw new BizVerifyException(
                    ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "centent和forwardHierarchy不能同时为空");
        }
        if (flow.getThemeId() == null && StringUtils.isEmpty(flow.getForwardHierarchy()) && flow.getScope() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "可见范围scope");
        }
        return Result.success(baseService.publishInformationFlow(flow));
    }

    @ApiOperation(value = "查询信息流详情", notes = "id不能为空")
    @PostMapping("/findWrapById")
    public Result findWrapById(@RequestBody InformationFlow flow) {
        if (flow.getId() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "id");
        }
        return Result.success(baseService.findWrapById(flow.getId()));
    }

    @ApiOperation(value = "统计新信息流条数", notes = "id不能为空")
    @PostMapping("/countNewInformation")
    public Result countNewInformation(@RequestBody InformationFlow flow) {
        if (flow.getId() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "id");
        }
        return Result.success(baseService.countNewInformation(flow.getId()));
    }

    @ApiOperation(value = "我发布的消息", notes = "分页查询 id为当前列表的最后记录id 如查询最新 则不传id size为取记录数")
    @PostMapping("/findPublishList")
    public Result findPublishList(@RequestBody BasePage<InformationFlow, Long> page) {
        if (page == null || page.getSize() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "分页参数");
        }
        return Result.success(baseService.findPublishList(page));
    }
}
