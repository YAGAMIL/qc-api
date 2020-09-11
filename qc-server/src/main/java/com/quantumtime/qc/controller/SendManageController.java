package com.quantumtime.qc.controller;

import com.google.zxing.WriterException;
import com.quantumtime.qc.common.controller.BaseController;
import com.quantumtime.qc.entity.undo.SendManage;
import com.quantumtime.qc.service.ISendManageService;
import com.quantumtime.qc.vo.MyPage;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * .Description:寄送管理 & Created on 2019/11/12 11:04
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@Api(tags = "寄送管理")
@RestController
@RequestMapping("/sendManage")
@Slf4j
public class SendManageController extends BaseController<SendManage, Long, ISendManageService> {

    @ApiOperation(value = "分页查询寄送", notes = "分页 按是否已寄送和时间倒序")
    @GetMapping("/list")
    public Result list(MyPage page) {
        log.info("list :  {} size : {}", page.getPage(), page.getSize());
        PageRequest rageRequest =
                PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by(Sort.Direction.DESC, "isSend", "sendTime"));
        return Result.success(baseService.findAll(rageRequest));
    }

    @ApiOperation(value = "修改寄送状态", notes = "修改寄送状态")
    @PostMapping("modifyIsSend")
    public Result modifyIsSend(@RequestBody SendManage entity) {
        log.info("modifyIsSend:  {}", entity);
        return Result.success(baseService.updateIsSend(entity.getId(), entity.getIsSend()));
    }

    @ApiOperation(value = "查看详情", notes = "查看详情", consumes = "application/x-www-form-urlencoded")
    @GetMapping("/viewDetails")
    public Result viewDetails(String sendMessageId) throws IOException, WriterException {
        return Result.success(baseService.viewDetails(Long.parseLong(sendMessageId)));
    }
}
