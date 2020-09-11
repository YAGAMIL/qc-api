package com.quantumtime.qc.controller;

import com.quantumtime.qc.entity.activity.Activity;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.IVideoService;
import com.quantumtime.qc.service.ActivityService;
import com.quantumtime.qc.vo.ActivityDetailVo;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 斗地主活动API Created on 2019/12/16 15:36
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Api(tags = "活动接口")
@RestController
@RequestMapping("/activity")
public class ActivityController {
  @Resource private ActivityService activityService;
  @Resource private IVideoService videoService;
  @Resource private AccountHelp accountHelp;

  @ApiOperation(value = "我所在生活圈的话题名称", notes = "返回当前生活圈活动的list")
  @PostMapping("/activityList")
  public Result myIntegralList(@RequestBody Address address) {
    Activity activity = activityService.activityList(address);
    if (activity==null) {
      return Result.success("该地区暂未有进行的活动，敬请期待", null);
    }
    return Result.success(activity);
  }

  @ApiOperation(value = "我的活动视频", notes = "返回我参与的最高热度视频,没有则返回null")
  @GetMapping("/my/video/activity/{activityId}")
  public Result myHottestVideo(@PathVariable("activityId") Long activityId) {
    String currentUid = accountHelp.getCurrentUser().getUid();
    return Result.success(videoService.findHottestVideo(currentUid, activityId));
  }

  @ApiOperation(value = "所有活动视频", notes = "分页返回参与活动的视频数据")
  @GetMapping("/video/activity/{activityId}/{pageNum}/{size}")
  public Result myHottestVideo(
      @PathVariable("activityId") Long activityId,
      @PathVariable("pageNum") Integer pageNum,
      @PathVariable("size") Integer size) {
    String currentUid = accountHelp.getCurrentUser().getUid();
    return Result.success(videoService.findActivityVideos(currentUid, activityId, pageNum, size));
  }


  @ApiOperation(value = "我所在生活圈的活动信息", notes = "我所在生活圈的活动信息")
  @GetMapping("/activityDetail/{poiId}")
  public Result activityDetail( @PathVariable ("poiId") String poiId ) {
    ActivityDetailVo activity = activityService.activityDetail(poiId);
    if (activity==null) {
      return Result.success("该地区暂未有进行的活动，敬请期待", null);
    }
    return Result.success(activity);
  }

  @ApiOperation(value = "附近poiIdList对应的活动", notes = "返回map<String,String>")
  @PostMapping(value = "/nearbyActivity")
  public Result nearbyActivity(@RequestBody List<String> poiIdList) {
    return Result.success(activityService.nearbyActivity(poiIdList));
  }




}
