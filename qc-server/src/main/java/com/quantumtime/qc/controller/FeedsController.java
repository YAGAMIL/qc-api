package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.controller.BaseController;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.entity.feeds.Feeds;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.FeedsService;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
;

/**
 * .Description:动态信息接口 & Created on 2019/11/12 10:52
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@Api(tags = "发布动态，")
@RestController
@RequestMapping("/feeds")
@Slf4j
public class FeedsController extends BaseController<Feeds, Long, FeedsService> {
    @Resource private FeedsService feedsService;

    @Resource private AccountHelp accountHelp;

    @ApiOperation(value = "发布动态", notes = "create_uid不能为空")
    @PostMapping("/addTrends")
    public Result addTrends(@RequestBody Feeds feeds) {
        log.info("zhuozhuo : {}", feeds);
        if (StringUtils.isEmpty(feeds.getContent()) && StringUtils.isEmpty(feeds.getPicture())) {
            throw new BizVerifyException(
                    ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "centent和forwardHierarchy不能同时为空");
        } else {
            feedsService.addTrends(feeds);
            return Result.success();
        }
    }

    @ApiOperation(value = "删除动态", notes = "需要id")
    @PostMapping("deleteTrends")
    public Result deleteTrends(@Param("feedsId") Long feedsId) {
        /*  if (feedsId == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "id");
        }*/
        feedsService.updateIsDelete(feedsId);
        return Result.success();
    }

    @ApiOperation(value = "返回1000条最近时间Trends", notes = "需要id")
    @GetMapping("return/{uid}")
    public Result trends(@PathVariable("uid") String uid) {
        /* if (id == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "id");
        }*/

        return Result.success(feedsService.trends(uid));
    }

    @ApiOperation(value = "动态详情", notes = "需要feedsId")
    @GetMapping("detail/{feedsId}")
    public Result trendsDetailed(@PathVariable("feedsId") Long feedsId) {
        /* if (id == null) {
            throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "id");
        }*/

        return Result.success(feedsService.trendsDetailed(feedsId));
    }

    @ApiOperation(value = "我的动态", notes = "")
    @PostMapping("myTrends")
    public Result myTrendsByUid(@RequestBody Map<String, Integer> map) {
        Integer pageNum = map.get("pageNum");
        Integer pageSize = map.get("pageSize");
        Assert.state(pageNum > 0, "pageNumber必须大于0");
        Assert.state(pageSize > 0, "pageNumber必须大于0");
        return Result.success(feedsService.myTrends(pageNum, pageSize));
    }

    @ApiOperation(value = "动态签发和废止", notes = "")
    @PostMapping("RepealFeeds/")
    public Result repealFeeds(@RequestBody Feeds feeds) {
        return Result.success(feedsService.RepealFeeds(feeds));
    }
}
