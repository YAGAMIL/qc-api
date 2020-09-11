package com.quantumtime.qc.controller;

import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.StarFanService;
import com.quantumtime.qc.vo.Result;
import com.quantumtime.qc.vo.StarFanListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .Description:用户关系API Program:qc-api.Created on 2019-11-13 14:23
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Slf4j
@Api(tags = "用户关系API")
@RestController
@RequestMapping("/nexus")
public class RelationController {

    @Resource private StarFanService starFanService;
    @Resource private AccountHelp accountHelp;

    @ApiOperation(value = "关注该用户", notes = "返回是否关注成功")
    @PutMapping("/subscribe/{starUid}")
    public Result click2Follow(@PathVariable("starUid") String starUid) {
        return Result.success(starFanService.attention(accountHelp.getCurrentUser(), starUid));
    }

    @ApiOperation(value = "取关该用户", notes = "返回是否取关成功")
    @DeleteMapping("/subscribe/{starUid}")
    public Result unsubscribe(@PathVariable("starUid") String starUid) {
        return Result.success(starFanService.unsubscribe(accountHelp.getCurrentUser().getUid(), starUid));
    }

    @ApiOperation(value = "显示已登录用户和批量用户的关系", notes = "返回uid 和关系type")
    @PostMapping("/subscribe")
    public Result starFan(@RequestBody StarFanListVo starFanListVo) {
        return Result.success(starFanService.starFanList(starFanListVo));
    }

    @ApiOperation(value = "获取其他用户的粉丝列表" , notes = "返回用户关系List")
    @GetMapping("/fansList/{otherUid}/{num}/{size}")
    public Result fansOfOther(
            @PathVariable("otherUid") String otherUid,
            @PathVariable("num") Integer num,
            @PathVariable("size") Integer size) {
        return Result.success(starFanService.getFanUsers(getCurrentUid(), otherUid, num, size));
    }

    @ApiOperation(value = "获取其他用户的关注列表" , notes = "返回用户关系List")
    @GetMapping("/starsList/{otherUid}/{num}/{size}")
    public Result starsOfOther(
            @PathVariable("otherUid") String otherUid,
            @PathVariable("num") Integer num,
            @PathVariable("size") Integer size) {
        return Result.success(starFanService.getStarUsers(getCurrentUid(), otherUid, num, size));
    }

    private String getCurrentUid() {
        User currentUser = accountHelp.getCurrentUser();
        return currentUser == null ? null : currentUser.getUid();
    }
}
