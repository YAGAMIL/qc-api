package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.controller.BaseController;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.common.model.BasePage;
import com.quantumtime.qc.entity.undo.Notification;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.INotificationService;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * .Description:通知管理。废弃 & Created on 2019/11/12 11:01
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@Api(tags = "通知管理")
@RestController
@RequestMapping("/notification")
@Slf4j
public class NotificationController extends BaseController<Notification, Long, INotificationService> {

    @Resource private AccountHelp accountHelp;

    @ApiOperation(
            value = "消息列表 支持分页",
            notes = "分页查询 id为当前列表的最后记录id 如查询最新 则不传id size为取记录数 不能为空 如果需要根据状态查询  传filters.status 状态 0：未读 1：已读")
    @PostMapping("/findPage")
    public Result findPage(@RequestBody BasePage<Notification, Long> page) {
        if (page == null || page.getSize() == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "分页参数");
        }
        User currentUser = accountHelp.getCurrentUser();
        Notification notification = new Notification();
        notification.setNotifiUserId(currentUser.getUid());
        page.setFilters(notification);
        return Result.success(this.baseService.findPage(page));
    }

    @ApiOperation(value = "统计未读消息数", notes = "")
    @PostMapping("/countUnRead")
    public Result countUnRead() {
        User currentUser = accountHelp.getCurrentUser();
        return Result.success(this.baseService.countUnReadNotification(currentUser.getUid()));
    }

    @ApiOperation(value = "聚合消息", notes = "")
    @PostMapping("/findAggregationNotificationList")
    public Result findAggregationNotificationList() {
        User currentUser = accountHelp.getCurrentUser();
        return Result.success(this.baseService.findAggregationNotificationList(currentUser.getUid()));
    }

    @ApiOperation(value = "根据id集合查询通知", notes = "")
    @PostMapping("/findByIds")
    public Result findByIds(@RequestBody List<Long> ids) {
        return Result.success(this.baseService.findByIds(ids));
    }

    @ApiOperation(value = "根据id集合设置为已读", notes = "")
    @PostMapping("/isReadByIds")
    public Result isReadByIds(@RequestBody List<Long> ids) {
        return Result.success(this.baseService.isReadByIds(ids));
    }
}
