package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.controller.BaseController;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.entity.information.InformationFlowCommit;
import com.quantumtime.qc.service.IInformationFlowCommitService;
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
 * .Description:评论接口 & Created on 2019/11/12 10:56
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@Api(tags = "信息流评论")
@RestController
@RequestMapping("/informationFlowCommit")
@Slf4j
public class InformationFlowCommitController extends BaseController<InformationFlowCommit, Long, IInformationFlowCommitService> {

    @ApiOperation(value = "评论", notes = "不需要添加id informationFlowId content 不能为空")
    @PostMapping("commit")
    public Result commit(@RequestBody InformationFlowCommit entity) {
        if(entity.getInformationFlowId() == null || StringUtils.isEmpty(entity.getContent())){
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "参数");
        }
        return Result.success(baseService.commitInformationFlow(entity));
    }

    @ApiOperation(value = "删除评论", notes = "需要id")
    @PostMapping("removeCommit")
    public Result removeCommit(@RequestBody InformationFlowCommit entity) {
        if(entity.getId() == null){
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "id");
        }
        return Result.success(baseService.removeCommitInformation(entity.getId()));
    }

    @ApiOperation(value = "查询评论详情", notes = "id不能为空")
    @PostMapping("/findWrapById")
    public Result findWrapById(@RequestBody InformationFlowCommit entity){
        if(entity.getId() == null){
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "id");
        }
        return Result.success(baseService.findWrapById(entity.getId()));
    }

    @ApiOperation(value = "信息流评论列表 支持分页", notes = "分页查询 id为当前列表的最后记录id 如查询最新 则不传id size为取记录数 传入信息流ID filters.informationFlowId 不可为空")
    @PostMapping("/findPage")
    public Result findPage(@RequestBody BasePage<InformationFlowCommit, Long> page){
        if(page == null || page.getSize() == null || page.getFilters() == null || page.getFilters().getInformationFlowId() == null){
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "参数");
        }
        return Result.success(this.baseService.findPage(page));
    }



}
