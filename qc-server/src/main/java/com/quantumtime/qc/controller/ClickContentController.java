package com.quantumtime.qc.controller;

import com.quantumtime.qc.service.ClickContentService;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * .Description:点击事件接口 & Created on 2019/11/12 10:50
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Api(tags = "点赞，")
@RestController
@RequestMapping("/clickContent")
@Slf4j
public class ClickContentController {
    private final ClickContentService clickContentService;

    public ClickContentController(ClickContentService clickContentService) {
        this.clickContentService = clickContentService;
    }

    @ApiOperation(value = "返回动态或者文章的详情list", notes = "")
    @PostMapping("/FeedsAndArticle")
    public Result myRecords(@RequestBody Map<String, Integer> map) {
        Integer pageNum = map.get("pageNum");
        Integer pageSize = map.get("pageSize");
        Assert.state(pageSize > 0, "当前页必须大于0");
        Assert.state(pageNum > 0, "每页条数必须大于0");
        return Result.success(clickContentService.myRecords(pageNum, pageSize));
    }

}
