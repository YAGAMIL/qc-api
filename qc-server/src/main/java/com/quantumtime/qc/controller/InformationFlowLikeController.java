package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.controller.BaseController;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.entity.information.InformationFlowLike;
import com.quantumtime.qc.service.IInformationFlowLikeService;
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
 * .Description: 信息流点赞接口 & Created on 2019/11/12 10:58
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@Api(tags = "信息流点赞")
@RestController
@RequestMapping("/informationFlowLike")
@Slf4j
public class InformationFlowLikeController
        extends BaseController<InformationFlowLike, Long, IInformationFlowLikeService> {

    @ApiOperation(value = "点赞", notes = "传informationFlowId")
    @PostMapping("like")
    public Result like(@RequestBody InformationFlowLike entity) {
        if (entity.getInformationFlowId() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "informationFlowId");
        }
        return Result.success(baseService.likeInformationFlow(entity));
    }

    @ApiOperation(value = "取消点赞", notes = "需要id")
    @PostMapping("removeLike")
    public Result removeLike(@RequestBody InformationFlowLike entity) {
        if (entity.getId() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "id");
        }
        return Result.success(baseService.removeLikeInformation(entity.getId()));
    }

    @ApiOperation(value = "查询点赞详情", notes = "id不能为空")
    @PostMapping("/findWrapById")
    public Result findWrapById(@RequestBody InformationFlowLike entity) {
        if (entity.getId() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "id");
        }
        return Result.success(baseService.findWrapById(entity.getId()));
    }

    @ApiOperation(
            value = "信息流点赞列表 支持分页",
            notes = "分页查询 id为当前列表的最后记录id 如查询最新 则不传id size为取记录数 传入信息流ID filters.informationFlowId 不可为空")
    @PostMapping("/findPage")
    public Result findPage(@RequestBody BasePage<InformationFlowLike, Long> page) {
        if (page == null
                || page.getSize() == null
                || page.getFilters() == null
                || page.getFilters().getInformationFlowId() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "参数");
        }
        return Result.success(this.baseService.findPage(page));
    }
}
