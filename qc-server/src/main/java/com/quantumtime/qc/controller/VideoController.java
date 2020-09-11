package com.quantumtime.qc.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.IVideoService;
import com.quantumtime.qc.vo.Result;
import com.quantumtime.qc.vo.video.*;
import com.quantumtime.qc.wrap.video.MyVideoWrap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.alibaba.druid.util.Utils.md5;
import static com.quantumtime.qc.constant.VodEventConstant.MIS;

@Api(tags = "视频接口")
@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {

    @Autowired
    private IVideoService videoService;

    @Autowired
    private AccountHelp accountHelp;

    @Value("${aliyun.vodCallbackUrl}")
    private String vodCallbackUrl;

    @Value("${aliyun.vodCallbackKey}")
    private String vodCallbackKey;

    @ApiOperation(value = "获取视频上传地址和凭证", notes = "返回VideoId,上传地址和凭证")
    @PostMapping("/uploadVideoAddress")
    public Result uploadVideoAddress(@RequestBody UploadVideoAddressRequest uploadVideoAddressRequest) {
        try {
            Assert.notNull(uploadVideoAddressRequest.getFileName(), "文件名为空");
            Assert.notNull(uploadVideoAddressRequest.getUid(), "用户为空");
            Assert.notNull(uploadVideoAddressRequest.getTitle(), "标题为空");
            Assert.notNull(accountHelp.getCurrentUser(), "用户必须登录才能上传");
            return Result.success(videoService.uploadVideoAddress(uploadVideoAddressRequest));
        } catch (Exception e) {
            log.error("uploadVideoAddress异常:", e);
            return Result.result(10601, "获取视频上传地址失败", null, e.getMessage());
        }
    }

    @ApiOperation(value = "运营后台获取视频上传地址和凭证", notes = "返回VideoId,上传地址和凭证")
    @PostMapping("/backendUploadVideoAddress")
    public Result backendUploadVideoAddress(@RequestBody UploadVideoAddressRequest uploadVideoAddressRequest) {
        try {
            Assert.notNull(uploadVideoAddressRequest.getFileName(), "文件名为空");
            Assert.notNull(uploadVideoAddressRequest.getUid(), "用户为空");
            Assert.notNull(uploadVideoAddressRequest.getTitle(), "标题为空");
            Assert.notNull(uploadVideoAddressRequest.getBackendIdentity(), "运营后台必须提供Identity");
            Assert.isTrue(uploadVideoAddressRequest.getSource() != null
                    && uploadVideoAddressRequest.getSource().equals(MIS), "Source必须为1");
            return Result.success(videoService.uploadVideoAddress(uploadVideoAddressRequest));
        } catch (Exception e) {
            log.error("backendUploadVideoAddress异常:", e);
            return Result.result(10600, "运营后台获取视频上传地址失败", null, e.getMessage());
        }
    }

    @ApiOperation(value = "刷新视频上传地址和凭证", notes = "返回上传地址和凭证")
    @GetMapping("/refreshUploadVideoAddress")
    public Result<RefreshUploadVideoResponse> refreshUploadVideoAddress(@RequestParam("videoId") String videoId) {
        try {
            return Result.success(videoService.refreshUploadVideoAddress(videoId));
        } catch (Exception e) {
            log.error("refreshUploadVideoAddress异常:", e);
            return Result.result(10602, "刷新视频上传地址失败", null, e.getMessage());
        }
    }

    @ApiOperation(value = "获取图片上传地址", notes = "返回ImageURL,ImageId,上传地址和凭据")
    @PostMapping("/uploadImageAddress")
    public Result<CreateUploadImageResponse> uploadImageAddress(
            @RequestBody UploadImageAddressRequest uploadImageAddressRequest) {
        try {
            Assert.notNull(uploadImageAddressRequest.getFileName(), "文件名为空");
            return Result.success(videoService.uploadImageAddress(uploadImageAddressRequest));
        } catch (Exception e) {
            log.error("uploadAddress异常:", e);
            return Result.result(10603, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "运营后台获取视频播放凭证", notes = "返回播放凭证")
    @GetMapping("/playVideoAuthBackend")
    public Result<GetVideoPlayAuthResponse> playVideoAuthBackend(
            @RequestParam String backendIdentity, @RequestParam String videoId) {
        try {
            return Result.success(videoService.playVideoAuth(backendIdentity, videoId));
        } catch (Exception e) {
            log.error("uploadAddress异常:", e);
            return Result.result(10609, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "获取视频播放凭证", notes = "返回播放凭证")
    @GetMapping("/playVideoAuth")
    public Result<GetVideoPlayAuthResponse> playVideoAuth(@RequestParam String videoId) {
        try {
            return Result.success(videoService.playVideoAuth(null, videoId));
        } catch (Exception e) {
            log.error("uploadAddress异常:", e);
            return Result.result(10604, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "获取视频播放信息", notes = "返回播放信息")
    @GetMapping("/playVideoInfo")
    public Result<GetPlayInfoResponse> playVideoInfo(@RequestParam String videoId) {
        try {
            return Result.success(videoService.playVideoInfo(videoId));
        } catch (Exception e) {
            log.error("playVideoInfo异常:", e);
            return Result.result(10610, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "删除视频")
    @DeleteMapping("/deleteVideo")
    public Result deleteVideo(@RequestParam String videoIds) {
        try {
            Assert.notNull(accountHelp.getCurrentUser(), "用户必须登录才能删除视频");
            videoService.deleteVideo(videoIds, null);
            return Result.success(true);
        } catch (Exception e) {
            log.error("deleteVideo异常:", e);
            return Result.result(10605, e.getMessage(), false, e.getMessage());
        }
    }

    @ApiOperation(value = "后台删除视频")
    @DeleteMapping("/backendDeleteVideo")
    public Result backendDeleteVideo(@RequestParam String videoIds, @RequestParam String backendIdentity) {
        try {
            Assert.notNull(backendIdentity, "后台删除视频必须提供identity");
            videoService.deleteVideo(videoIds, backendIdentity);
            return Result.success(true);
        } catch (Exception e) {
            log.error("backendDeleteVideo异常:", e);
            return Result.result(10611, e.getMessage(), false, e.getMessage());
        }
    }

    @ApiOperation(value = "回调处理")
    @PostMapping("/callback")
    public Result callback(HttpServletRequest httpRequest, @RequestBody String body) {
        final String currentTime = httpRequest.getHeader("X-VOD-TIMESTAMP");
        final String sign = httpRequest.getHeader("X-VOD-SIGNATURE");
        String md5Content = vodCallbackUrl + "|" + currentTime + "|" + vodCallbackKey;
        String md5Sign = md5(md5Content);
        try {
            if (sign.equals(md5Sign)) {
                videoService.callback(body);
                if (log.isDebugEnabled()) {
                    log.debug("回调鉴权成功");
                }
                return Result.success();
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("回调鉴权失败");
                }
                return Result.error400("请求无效", "http回调鉴权失败");
            }
        } catch (Exception e) {
            log.error("callback异常:", e);
            return Result.result(10606, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "STS凭证")
    @GetMapping("/sts")
    public Result sts() {
        try {
            return Result.success(videoService.getSts());
        } catch (Exception e) {
            log.error("sts异常:", e);
            return Result.result(10609, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "视频首页")
    @PostMapping("/portal")
    public Result portal(@RequestBody PortalRequest portalRequest) {
        if (log.isDebugEnabled()) {
            log.debug("PortalRequest:" + portalRequest.toString());
        }
        try {
            Assert.notNull(portalRequest.getRequestId(), "请求Id为空");
            // Assert.notNull(portalRequest.getAddress(), "地址为空");
            Assert.notNull(portalRequest.getPageNum(), "页数为空");
            Assert.notNull(portalRequest.getPageSize(), "每页数目为空");
            PortalResponse response = videoService.getPortalResponse(portalRequest);
            if (log.isDebugEnabled()) {
                log.debug("PortalResponse:" + response.toString());
            }
            return Result.success(response);
        } catch (Exception e) {
            log.error("portal异常:", e);
            return Result.result(10607, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "视频详情")
    @GetMapping("/detail")
    public Result detail(@RequestParam String videoId, @RequestParam(required = false) String uid) {
        if (log.isDebugEnabled()) {
            log.debug("video info id:" + videoId.toString() + " uid:" + uid);
        }
        try {
            VideoItem videoItem = videoService.getDetail(videoId, uid);
            if (log.isDebugEnabled()) {
                log.debug("videoItem:" + JSONObject.toJSONString(videoItem));
            }
            return Result.success(videoItem);
        } catch (Exception e) {
            log.error("detail异常:", e);
            return Result.result(10608, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "我的作品集合")
    @PostMapping("/opusList")
    public Result opusList(@RequestBody MyVideoWrap myVideoWrap) {
        Integer pageNum = myVideoWrap.getPageNum();
        Integer pageSize = myVideoWrap.getPageSize();
        Assert.state(pageNum > 0, "pageNum必须大于0");
        Assert.state(pageSize > 0, "pageSize必须大于0");
        return Result.success(videoService.personalVideoList(myVideoWrap));
    }

    @ApiOperation(value = "我点过赞的作品集合")
    @PostMapping("/likeOpusList")
    public Result likeOpusList(@RequestBody MyVideoWrap myVideoWrap) {
        Integer pageNum = myVideoWrap.getPageNum();
        Integer pageSize = myVideoWrap.getPageSize();
        Assert.state(pageNum > 0, "pageNum必须大于0");
        Assert.state(pageSize > 0, "pageSize必须大于0");
        return Result.success(videoService.personaLikeVideoList(myVideoWrap));
    }

    @ApiOperation(value = "地主视频列表")
    @PostMapping("/addressVideoList")
    public Result addressVideoList(@RequestBody PortalRequest portalRequest) {
        if (log.isDebugEnabled()) {
            log.debug("addressVideoList:" + portalRequest.toString());
        }
        try {
            Assert.notNull(portalRequest.getAddress(), "地址为空");
            Assert.notNull(portalRequest.getPageNum(), "页数为空");
            Assert.notNull(portalRequest.getPageSize(), "每页数目为空");
            PortalResponse response = videoService.getAddressVideoList(portalRequest);
            if (log.isDebugEnabled()) {
                log.debug("AddressVideoList:" + response.toString());
            }
            return Result.success(response);
        } catch (Exception e) {
            log.error("AddressVideoList:", e);
            return Result.result(10612, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "举报视频")
    @PostMapping("/reportVideo")
    public Result reportVideo(@RequestBody ReportVideoRequest reportVideoRequest) {
        if (log.isDebugEnabled()) {
            log.debug("reportVideo:" + reportVideoRequest.toString());
        }
        try {
            Assert.notNull(reportVideoRequest.getUid(), "被举报用户uid为空");
            Assert.notNull(reportVideoRequest.getVideoId(), "videoId为空");
            Assert.notNull(reportVideoRequest.getReason(), "reason为空");
            User user = accountHelp.getCurrentUser();
            reportVideoRequest.setReportUid(user.getUid());
            videoService.reportVideo(reportVideoRequest);
            return Result.success();
        } catch (Exception e) {
            log.error("reportVideo:", e);
            return Result.result(10613, e.getMessage(), null, e.getMessage());
        }
    }

    @ApiOperation(value = "分享视频")
    @PutMapping("/share")
    public Result shareAdd(@PathVariable String  videoId) {
        videoService.shareVideo(videoId);
        return Result.success();
    }

}
