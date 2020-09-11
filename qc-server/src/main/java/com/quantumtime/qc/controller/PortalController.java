package com.quantumtime.qc.controller;

import com.quantumtime.qc.service.IRecommendService;
import com.quantumtime.qc.vo.Result;
import com.quantumtime.qc.vo.recommend.SquareResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "首页")
@RestController
@RequestMapping("/portal")
@Slf4j
public class PortalController {

    @Autowired
    private IRecommendService recommendService;

    @ApiOperation(value = "广场信息", notes = "返回推荐信息列表")
    @PostMapping("/square")
    public Result<SquareResponse> squareFeeds(@RequestBody Map<String, Integer> pageMap) {
        Integer pageNum = pageMap.get("pageNum");
        Integer pageSize = pageMap.get("pageSize");
        Assert.state(pageSize > 0, "当前页必须大于0");
        Assert.state(pageNum > 0, "每页条数必须大于0");
        long start = System.currentTimeMillis();
        SquareResponse squareResponse = recommendService.getSquareResponse(pageNum, pageSize);
        if (log.isDebugEnabled()) {
            log.debug("Square total response time:" + (System.currentTimeMillis() - start) + "ms");
        }
        return Result.success(squareResponse);
    }
}
