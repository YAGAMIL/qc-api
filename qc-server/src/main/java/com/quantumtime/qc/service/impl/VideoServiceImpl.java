package com.quantumtime.qc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.DeleteImageRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetMediaAuditResultRequest;
import com.aliyuncs.vod.model.v20170321.GetMediaAuditResultResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetTranscodeSummaryRequest;
import com.aliyuncs.vod.model.v20170321.GetTranscodeSummaryResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.quantumtime.qc.common.exception.ExpFunction;
import com.quantumtime.qc.common.utils.OptionalConsumer;
import com.quantumtime.qc.entity.ClickContent;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.entity.feeds.Video;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.report.ReportVideo;
import com.quantumtime.qc.repository.ClickContentRepository;
import com.quantumtime.qc.repository.ReportVideoRepository;
import com.quantumtime.qc.repository.VideoRepository;
import com.quantumtime.qc.service.ClickContentService;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.service.IRedisService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.service.IVideoRecommendService;
import com.quantumtime.qc.service.IVideoService;
import com.quantumtime.qc.service.LegalCheckService;
import com.quantumtime.qc.service.StarFanService;
import com.quantumtime.qc.vo.video.PortalRequest;
import com.quantumtime.qc.vo.video.PortalResponse;
import com.quantumtime.qc.vo.video.ReportVideoRequest;
import com.quantumtime.qc.vo.video.UploadImageAddressRequest;
import com.quantumtime.qc.vo.video.UploadVideoAddressRequest;
import com.quantumtime.qc.vo.video.VideoIDSrouce;
import com.quantumtime.qc.vo.video.VideoItem;
import com.quantumtime.qc.wrap.RelationInfo;
import com.quantumtime.qc.wrap.video.MyVideoWrap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.quantumtime.qc.common.constant.ErrorCodeConstant.ACCOUNT_NOT_AUTH;
import static com.quantumtime.qc.common.utils.GlobalUtils.getNullNumElse;
import static com.quantumtime.qc.constant.CoverEventConstant.MY_CHECK_BLOCK;
import static com.quantumtime.qc.constant.CoverEventConstant.MY_CHECK_PASS;
import static com.quantumtime.qc.constant.CoverEventConstant.MY_CHECK_REVIEW;
import static com.quantumtime.qc.constant.CoverEventConstant.SNAPSHOTS;
import static com.quantumtime.qc.constant.VodEventConstant.AI_DATA;
import static com.quantumtime.qc.constant.VodEventConstant.AI_MEDIA_AUDIT_COMPLETE;
import static com.quantumtime.qc.constant.VodEventConstant.APP;
import static com.quantumtime.qc.constant.VodEventConstant.AUDIT_STATUS;
import static com.quantumtime.qc.constant.VodEventConstant.CREATE_AUDIT_COMPLETE;
import static com.quantumtime.qc.constant.VodEventConstant.EVENT_TYPE;
import static com.quantumtime.qc.constant.VodEventConstant.EXTEND;
import static com.quantumtime.qc.constant.VodEventConstant.FILE_UPLOAD_COMPLETE;
import static com.quantumtime.qc.constant.VodEventConstant.FILE_URL;
import static com.quantumtime.qc.constant.VodEventConstant.HEIGHT;
import static com.quantumtime.qc.constant.VodEventConstant.MEDIA_RESP;
import static com.quantumtime.qc.constant.VodEventConstant.MIS;
import static com.quantumtime.qc.constant.VodEventConstant.NORMAL;
import static com.quantumtime.qc.constant.VodEventConstant.PASS;
import static com.quantumtime.qc.constant.VodEventConstant.REVIEW;
import static com.quantumtime.qc.constant.VodEventConstant.SNAPSHOT_COMPLETE;
import static com.quantumtime.qc.constant.VodEventConstant.STREAM_TRANSCODE_COMPLETE;
import static com.quantumtime.qc.constant.VodEventConstant.SUGGESTION;
import static com.quantumtime.qc.constant.VodEventConstant.UNKNOWN_SOURCE;
import static com.quantumtime.qc.constant.VodEventConstant.VIDEO_FROM_FIELD;
import static com.quantumtime.qc.constant.VodEventConstant.VIDEO_RESP;
import static com.quantumtime.qc.constant.VodEventConstant.VOD_STATUS_FIELD;
import static com.quantumtime.qc.constant.VodEventConstant.VOD_STATUS_SUCCESS;
import static com.quantumtime.qc.constant.VodEventConstant.WIDTH;
import static com.quantumtime.qc.entity.feeds.Video.STATUS_AUDIT_COMPLETE;
import static com.quantumtime.qc.entity.feeds.Video.STATUS_AUDIT_FAIL;
import static com.quantumtime.qc.entity.feeds.Video.STATUS_AUDIT_REVIEW;
import static com.quantumtime.qc.entity.feeds.Video.STATUS_CUT_FAIL;
import static com.quantumtime.qc.entity.feeds.Video.STATUS_DELETED;
import static com.quantumtime.qc.entity.feeds.Video.STATUS_UPLOADED;

/**
 * The type Video service.
 */
@SuppressWarnings("FieldCanBeLocal")
@Service
@Slf4j
@Configuration
public class VideoServiceImpl implements IVideoService {
    /*当前 STS API 版本*/
    private static final String STS_API_VERSION = "2015-04-01";
    private static final String STS_KEY = "STS";
    private static final long STS_EXPIRATION = 3500;
    private static String VIDEO_KEY_PREFIX = "VIDEO_";
    private static String VIDEO_KEY_URL_PREFIX = "VIDEO_URL";
    private static String PORTAL_REQUEST_KEY_PREFIX = "PORTAL_";
    private static String ADDRESS_VIDEO_REQUEST_KEY_PREFIX = "ADDRESS_VIDEO_";
    private static long VIDEO_CACHE_EXPIRATION = 2900;
    private static int SHUFFLE_PERIOD = 2 * 3600 * 1000;
    private static int MY_CLICK_VIDEO_LIST = 1;
    private static int MYSELF_VIDEO_LIST = 2;
    private static int VIDEO_SOURCE_BUSINESS = 1;

    @Resource
    LegalCheckService legalCheckService;
    @Resource
    private DefaultAcsClient defaultAcsClient;
    @Resource
    private VideoRepository videoRepository;
    @Resource
    private IRedisService redisService;
    @Resource
    private IAddressService addressService;
    @Resource
    private IUserService userService;
    @Resource
    private IVideoRecommendService videoRecommendService;
    @Resource
    private ClickContentRepository clickContentRepository;
    @Resource
    private ClickContentService clickContentService;
    @Resource
    private ReportVideoRepository reportVideoRepository;

    @Resource
    private StarFanService starFanService;

    @Value("${video.openDay}")
    private int openDay;

    @Value("${aliyun.vodWorkFlowId}")
    private String vodWorkFlowId;

    @Value("${aliyun.vodCallbackUrl}")
    private String vodCallbackUrl;

    @Value("${video.recommendCacheTime}")
    private long recommendCacheTime;

    @Value("${video.backendIdentity}")
    private String backendIdentity;

    @Value("${aliyun.vodAccessKeyId}")
    private String vodAccessKeyId;

    @Value("${aliyun.vodAccessKeySecret}")
    private String vodAccessKeySecret;

    @Value("${aliyun.vodStsRegionId}")
    private String vodStsRegionId;

    @Value("${aliyun.vodRoleArn}")
    private String vodRoleArn;

    @Value("${video.checkTimeout}")
    private long checkTimeout;

    @Override
    public CreateUploadVideoResponse uploadVideoAddress(UploadVideoAddressRequest uploadVideoAddressRequest)
            throws ClientException {
        if (log.isDebugEnabled()) {
            log.debug("uploadVideoAddress request:" + JSONObject.toJSONString(uploadVideoAddressRequest));
        }
        Video video = new Video();
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        String title = uploadVideoAddressRequest.getTitle();
        String cateId = uploadVideoAddressRequest.getCateId();
        String coverUrl = uploadVideoAddressRequest.getCoverUrl();
        String coverImageId = uploadVideoAddressRequest.getCoverImageId();
        String tags = uploadVideoAddressRequest.getTags();
        String description = uploadVideoAddressRequest.getDescription();
        String fileSize = uploadVideoAddressRequest.getFileSize();
        Long activityId = uploadVideoAddressRequest.getActivityId();
        String activityName = uploadVideoAddressRequest.getActivityName();
        Optional.ofNullable(activityId).ifPresent(video::setActivityId);
        Optional.ofNullable(activityName).filter(StringUtils::isNotBlank).ifPresent(video::setActivityName);
        Optional.ofNullable(title).filter(StringUtils::isNotBlank).map(video::setTitle).ifPresent(v -> request.setTitle(v.getTitle()));
        if (StringUtils.isNotEmpty(cateId)) {
            request.setCateId(Long.parseLong(cateId));
            video.setCateId(cateId);
        }
        if (StringUtils.isNotEmpty(coverUrl)) {
            request.setCoverURL(coverUrl);
            video.setCoverUrl(coverUrl);
        }
        Optional.ofNullable(coverImageId).filter(StringUtils::isNotBlank).ifPresent(video::setCoverImageId);
        if (StringUtils.isNotBlank(description)) {
            request.setDescription(description);
            video.setDescription(legalCheckService.checkText(description));
        }
        if (StringUtils.isNotEmpty(fileSize)) {
            request.setFileSize(Long.parseLong(fileSize));
            video.setFileSize(fileSize);
        }
        if (StringUtils.isNotEmpty(tags)) {
            request.setTags(tags);
            video.setTags(tags);
        }
        String fileName = uploadVideoAddressRequest.getFileName();
        request.setFileName(fileName);
        request.setWorkflowId(vodWorkFlowId);
        video.setFileName(fileName).setWorkFlowId(vodWorkFlowId);
        int source = Optional.ofNullable(uploadVideoAddressRequest.getSource()).orElse(APP);
        boolean isMIS = source == MIS;
        ExpFunction.true4ThrowBiz(
                isMIS && !uploadVideoAddressRequest.getBackendIdentity().equals(this.backendIdentity),
                ACCOUNT_NOT_AUTH,
                "运营后台身份为非法");
        video.setMisCreateTime(new Date());
        JSONObject userData = new JSONObject();
        JSONObject messageCallback = new JSONObject();
        messageCallback.put("CallbackURL", vodCallbackUrl);
        messageCallback.put("CallbackType", "http");
        userData.put("MessageCallback", messageCallback.toJSONString());
        JSONObject extend = new JSONObject();
        extend.put(VIDEO_FROM_FIELD, source == 0 ? APP : MIS);
        userData.put(EXTEND, extend.toJSONString());
        request.setUserData(userData.toJSONString());
        CreateUploadVideoResponse response = defaultAcsClient.getAcsResponse(request);
        video.setVideoId(response.getVideoId());
        video.setUid(uploadVideoAddressRequest.getUid());
        Address shotPlace = uploadVideoAddressRequest.getShotPlace();
        if (shotPlace != null) {
            video.setAddressId(getAddressId(shotPlace))
                    .setLongitude(shotPlace.getLongitude())
                    .setLatitude(shotPlace.getLatitude());
        }
        Address scopeAddress = uploadVideoAddressRequest.getScopeAddress();
        video = scopeAddress != null
                ? video.setScope(Video.SCOPE_POI).setScopeAddressId(getAddressId(scopeAddress))
                : video.setScope(Video.SCOPE_ALL);

        video.setCreateTime(new Date()).setStatus(Video.STATUS_UPLOADING).setSource(source);
        videoRepository.save(video);
        if (log.isDebugEnabled()) {
            log.debug("uploadVideoAddress response:" + JSONObject.toJSONString(response));
        }
        return response;
    }

    @Override
    public RefreshUploadVideoResponse refreshUploadVideoAddress(String videoId) throws ClientException {
        if (log.isDebugEnabled()) {
            log.debug("refreshUploadVideoAddress videoId:" + videoId);
        }
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        request.setVideoId(videoId);
        RefreshUploadVideoResponse response = defaultAcsClient.getAcsResponse(request);
        if (log.isDebugEnabled()) {
            log.debug("refreshUploadVideoAddress response:" + JSONObject.toJSONString(response));
        }
        return response;
    }

    @Override
    public CreateUploadImageResponse uploadImageAddress(UploadImageAddressRequest uploadImageAddressRequest)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("uploadImageAddress request:" + JSONObject.toJSONString(uploadImageAddressRequest));
        }
        // 获取扩展名
        String ext = StringUtils.substringAfterLast(uploadImageAddressRequest.getFileName(), ".").toLowerCase();
        ExpFunction.true4ThrowBiz(!checkExt(ext), "500", "图片格式不对");
        CreateUploadImageRequest request = new CreateUploadImageRequest();
        request.setImageType("cover");
        request.setImageExt(ext);
        if (StringUtils.isNotEmpty(uploadImageAddressRequest.getTitle())) {
            request.setTitle(uploadImageAddressRequest.getTitle());
        }
        if (StringUtils.isNotEmpty(uploadImageAddressRequest.getCateId())) {
            request.setCateId(Long.parseLong(uploadImageAddressRequest.getCateId()));
        }
        if (StringUtils.isNotEmpty(uploadImageAddressRequest.getDescription())) {
            request.setDescription(uploadImageAddressRequest.getDescription());
        }
        if (StringUtils.isNotEmpty(uploadImageAddressRequest.getTags())) {
            request.setTags(uploadImageAddressRequest.getTags());
        }
        JSONObject userData = new JSONObject();

        JSONObject messageCallback = new JSONObject();
        messageCallback.put("CallbackURL", vodCallbackUrl);
        messageCallback.put("CallbackType", "http");
        userData.put("MessageCallback", messageCallback.toJSONString());
        request.setUserData(userData.toJSONString());
        CreateUploadImageResponse response = defaultAcsClient.getAcsResponse(request);
        if (log.isDebugEnabled()) {
            log.debug("uploadImageAddress response:" + JSONObject.toJSONString(response));
        }
        return response;
    }

    private boolean checkExt(String ext) {
        return StringUtils.isNotEmpty(ext) && "png,jpg,jpeg,gif".contains(ext);
    }

    private Long getAddressId(Address shotPlace) {
        return addressService.checkAndSet(shotPlace);
    }

    @Override
    public GetVideoPlayAuthResponse playVideoAuth(String backendIdentity, String videoId) throws ClientException {
        boolean isBackendRequest = false;
        if (StringUtils.isNotEmpty(backendIdentity) && backendIdentity.equals(this.backendIdentity)) {
            isBackendRequest = true;
        }

        if (log.isDebugEnabled()) {
            log.debug("playVideoAuth videoId:" + videoId + " backendIdentity:" + backendIdentity);
        }
        Video video = videoRepository.getOne(videoId);
        if (auth(isBackendRequest, video)) {
            return null;
        }

        //noinspection unchecked
        GetVideoPlayAuthResponse response = (GetVideoPlayAuthResponse) redisService.get(VIDEO_KEY_PREFIX + videoId);
        if (response == null) {
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            request.setAuthInfoTimeout(3000L);
            response = defaultAcsClient.getAcsResponse(request);
            redisService.set(VIDEO_KEY_PREFIX + videoId, response, VIDEO_CACHE_EXPIRATION);
        }
        if (log.isDebugEnabled()) {
            log.debug("playVideoAuth response:" + JSONObject.toJSONString(response));
        }
        return response;
    }

    private boolean auth(boolean isBackendRequest, Video video) {
        return (!isBackendRequest && !video.getStatus().equals(Video.STATUS_AUDIT_COMPLETE))
                || (isBackendRequest
                && !video.getStatus().equals(Video.STATUS_UPLOADED)
                && !video.getStatus().equals(Video.STATUS_AUDIT_COMPLETE)
                && !video.getStatus().equals(Video.STATUS_GIVEN_UP)
                && !video.getStatus().equals(Video.STATUS_DISABLE)
                && !video.getStatus().equals(STATUS_AUDIT_REVIEW)
                && !video.getStatus().equals(STATUS_AUDIT_FAIL));
    }

    @Override
    public GetPlayInfoResponse playVideoInfo(String videoId) throws ClientException {
        if (log.isDebugEnabled()) {
            log.debug("playVideoInfo videoId:" + videoId);
        }
        Video video = videoRepository.getOne(videoId);
        if (!video.getStatus().equals(Video.STATUS_AUDIT_COMPLETE)) {
            return null;
        }
        //noinspection unchecked
        GetPlayInfoResponse response = (GetPlayInfoResponse) redisService.get(VIDEO_KEY_URL_PREFIX + videoId);
        if (response == null) {
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(videoId);
            response = defaultAcsClient.getAcsResponse(request);
            adjustCoverUrl(video, response);
            redisService.set(VIDEO_KEY_URL_PREFIX + videoId, response, VIDEO_CACHE_EXPIRATION);
        }
        if (log.isDebugEnabled()) {
            log.debug("playVideoInfo response:" + JSONObject.toJSONString(response));
        }
        return response;
    }

    private void adjustCoverUrl(Video video, GetPlayInfoResponse response) {
        if (response.getVideoBase().getCoverURL() == null || "-".equals(response.getVideoBase().getCoverURL())) {
            response.getVideoBase().setCoverURL(video.getCoverUrl());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteVideo(String videoIds, String identity) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("deleteVideo videoIds:" + videoIds);
        }
        if (identity != null && !identity.equals(backendIdentity)) {
            throw new Exception("运营后台的identity错误");
        }
        String[] ary = videoIds.split(",");
        if (ary.length > 20) {
            throw new Exception("一次删除不能超过20个");
        }
        List<String> list = Arrays.asList(ary);
        videoRepository.setDelete(list);
        // 删除视频
        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoIds);
        AcsResponse response = defaultAcsClient.getAcsResponse(request);
        if (log.isDebugEnabled()) {
            log.debug("deleteVideo response:" + JSONObject.toJSONString(response));
        }
        // 删除封面图片
        List<Video> videoList = videoRepository.findAllById(list);
        if (videoList.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Video video : videoList) {
            if (StringUtils.isNotEmpty(video.getCoverImageId())) {
                sb.append(video.getCoverImageId()).append(',');
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            DeleteImageRequest deleteImageRequest = new DeleteImageRequest();
            deleteImageRequest.setDeleteImageType("ImageId");
            deleteImageRequest.setImageIds(sb.toString());
            AcsResponse deleteImageResponse = defaultAcsClient.getAcsResponse(deleteImageRequest);
            if (log.isDebugEnabled()) {
                log.debug("deleteImages response:" + JSONObject.toJSONString(deleteImageResponse));
            }
        }
    }

    @Override
    public void callback(String jsonStr) {
        log.info("callback event:" + jsonStr);
        JSONObject event = JSON.parseObject(jsonStr);
        if (getSource(event) == UNKNOWN_SOURCE) {
            log.debug("视频来源未知，不做处理");
            return;
        }
        String media = event.getString(MEDIA_RESP);
        String videoId = StringUtils.isNotBlank(media) ? media : event.getString(VIDEO_RESP);
        Optional<Video> videoPackage = videoRepository.findById(videoId);
        OptionalConsumer.of(videoPackage).ifPresent(video -> matchExecution(event, video)).ifNotPresent(() ->
                log.error("video id:" + videoId + "不存在"));
    }

    @Override
    public PortalResponse getPortalResponse(PortalRequest portalRequest) throws Exception {
        long start = System.currentTimeMillis();
        PortalResponse portalResponse = new PortalResponse();
        // 从缓存里读取当前请求，没有的话生成一个集合
        List<VideoIDSrouce> recommendList =
                (List<VideoIDSrouce>) redisService.get(PORTAL_REQUEST_KEY_PREFIX + portalRequest.getRequestId());
        if (recommendList == null) {
            recommendList = videoRecommendService.recommend(portalRequest);
            redisService.set(
                    PORTAL_REQUEST_KEY_PREFIX + portalRequest.getRequestId(), recommendList, recommendCacheTime);
        }
        if (recommendList.isEmpty()) {
            portalResponse.setCount(0);
            portalResponse.setRest(0);
            return portalResponse;
        }
        // 计算拿出哪一页
        int startPos = getStartPos(portalRequest);
        if (startPos > recommendList.size() - 1) {
            // 已经没有了
            portalResponse.setCount(0);
            portalResponse.setRest(0);
            return portalResponse;
        }
        int endPos = startPos + portalRequest.getPageSize();
        if (endPos >= recommendList.size()) {
            endPos = recommendList.size();
            portalResponse.setRest(0);
        } else {
            portalResponse.setRest(recommendList.size() - endPos);
        }
        VideoHandler videoHandler = new VideoHandler(portalRequest, recommendList, startPos, endPos).invoke();
        List<String> userVideoIds = videoHandler.getUserVideoIds();
        List<Video> videoList = videoHandler.getVideoList();
        Map<String, User> userMap = videoHandler.getUserMap();
        Map<Long, Address> addressMap = videoHandler.getAddressMap();
        Map<String, RelationInfo> relationMap = videoHandler.getRelationMap();

        // 添加点赞数和当前用户是否点赞
        buildItems(portalResponse, userVideoIds, videoList, userMap, addressMap, portalRequest.getUid(), relationMap, null);
        log.info("请求Id:"
                + portalRequest.getRequestId()
                + " pageNum:"
                + portalRequest.getPageNum()
                + " 耗时:"
                + (System.currentTimeMillis() - start)
                + "ms");
        return portalResponse;
    }

    private int getStartPos(PortalRequest portalRequest) {
        return (portalRequest.getPageNum() - 1) * portalRequest.getPageSize();
    }

    @Override
    public PortalResponse getAddressVideoList(PortalRequest portalRequest) {
        long start = System.currentTimeMillis();
        PortalResponse portalResponse = new PortalResponse();
        // 从缓存里读取当前请求，没有的话生成一个集合
        List<VideoIDSrouce> recommendList =
                (List<VideoIDSrouce>) redisService.get(ADDRESS_VIDEO_REQUEST_KEY_PREFIX + portalRequest.getRequestId());
        if (recommendList == null) {
            recommendList = videoRecommendService.addressVideoList(portalRequest);
            redisService.set(
                    ADDRESS_VIDEO_REQUEST_KEY_PREFIX + portalRequest.getRequestId(), recommendList, recommendCacheTime);
        }
        if (recommendList.isEmpty()) {
            portalResponse.setCount(0);
            portalResponse.setRest(0);
            return portalResponse;
        }
        // 计算拿出哪一页
        int startPos = getStartPos(portalRequest);
        if (startPos > recommendList.size() - 1) {
            // 已经没有了
            portalResponse.setCount(0);
            portalResponse.setRest(0);
            return portalResponse;
        }
        int endPos = startPos + portalRequest.getPageSize();
        if (endPos >= recommendList.size()) {
            endPos = recommendList.size();
            portalResponse.setRest(0);
        } else {
            portalResponse.setRest(recommendList.size() - endPos);
        }
        //获取地主的uid
        Video firstVideo = videoRepository.getOne(recommendList.get(0).getVideoID());
        String landOwnerUid = firstVideo.getUid();

        VideoHandler videoHandler = getVideoHandler(portalRequest, recommendList, startPos, endPos);
        List<String> userVideoIds = videoHandler.getUserVideoIds();
        List<Video> videoList = videoHandler.getVideoList();
        Map<String, User> userMap = videoHandler.getUserMap();
        Map<Long, Address> addressMap = videoHandler.getAddressMap();
        Map<String, RelationInfo> relationMap = videoHandler.getRelationMap();

        // 添加点赞数和当前用户是否点赞
        buildItems(portalResponse, userVideoIds, videoList, userMap, addressMap, portalRequest.getUid(), relationMap, landOwnerUid);
        log.info("地主视频列表请求Id:"
                + portalRequest.getRequestId()
                + " pageNum:"
                + portalRequest.getPageNum()
                + " 耗时:"
                + (System.currentTimeMillis() - start)
                + "ms");
        return portalResponse;
    }

    private VideoHandler getVideoHandler(PortalRequest portalRequest, List<VideoIDSrouce> recommendList, int startPos, int endPos) {
        return new VideoHandler(portalRequest, recommendList, startPos, endPos).invoke();
    }

    private void buildItems(
            PortalResponse portalResponse,
            List<String> userVideoIds,
            List<Video> videoList,
            Map<String, User> userMap,
            Map<Long, Address> addressMap,
            String uid,
            Map<String, RelationInfo> relationMap,
            String landOwnerUid) {
        Map<String, Integer> likeMap = clickContentService.getClickMap(userVideoIds, true);
        Map<String, Integer> viewMap = clickContentService.getClickMap(userVideoIds, false);
        Map<String, Boolean> userLikeMap = getUserLikeMap(uid, userVideoIds);
        List<VideoItem> videoItems = new ArrayList<>();
        Set<String> blacklist = videoRecommendService.getBlacklist(uid);
        videoList.stream().filter(v ->
            v != null && !blacklist.contains(v.getUid())).forEach(video ->
                Optional.ofNullable(
                        createVideoItem(addressMap, userMap, likeMap, viewMap, userLikeMap, video, relationMap, landOwnerUid))
                        .ifPresent(videoItems::add));
        portalResponse.setCount(videoItems.size());
        portalResponse.setVideoList(videoItems);
    }

    private VideoItem createVideoItem(
            Map<Long, Address> addressMap,
            Map<String, User> userMap,
            Map<String, Integer> likeMap,
            Map<String, Integer> viewMap,
            Map<String, Boolean> userLikeMap,
            Video video,
            Map<String, RelationInfo> relationMap,
            String landOwnerUid) {
        VideoItem videoItem = new VideoItem();
        User user = userMap.get(video.getUid());
        if (user == null) {
            return null;
        }
        BeanUtils.copyProperties(user, videoItem);
        BeanUtils.copyProperties(video, videoItem);
        Address address = Optional.ofNullable(video.getAddressId()).map(addressMap::get).orElse(null);
        Optional.ofNullable(relationMap.get(video.getUid()))
                .ifPresent(code -> videoItem.setRelationCode(code.getRelationCode()));
        Optional.ofNullable(address).ifPresent(a -> videoItem.setLatitude(address.getLatitude()).setLongitude(a.getLongitude()));
        videoItem.setPoiName(address != null ? address.getPoiName() : (video.getSource() == 1 ? "热门" : "周边"));
        videoItem.setNickName(user.getNickname());
        videoItem.setView(Optional.ofNullable(viewMap.get(video.getVideoId())).orElse(0));
        videoItem.setLike(Optional.ofNullable(likeMap.get(video.getVideoId())).orElse(0));
        Boolean userLike = userLikeMap.get(video.getVideoId());
        videoItem.setIsUserLike(userLike != null && userLike);
        videoItem.setUserAddressId(user.getAddressId());
        videoItem.setIsLandOwner(Objects.equals(landOwnerUid, video.getUid()));
        return videoItem;
    }



    @Override
    public PortalResponse personalVideoList(MyVideoWrap myVideoWrap) {

        return videoList(myVideoWrap, MYSELF_VIDEO_LIST);
    }

    @Override
    public PortalResponse personaLikeVideoList(MyVideoWrap myVideoWrap) {

        return videoList(myVideoWrap, MY_CLICK_VIDEO_LIST);
    }

    private PortalResponse videoList(MyVideoWrap myVideoWrap, int videoListType) {
        PortalResponse portalResponse = new PortalResponse();
        List<Video> videoList = new ArrayList<>();
        Integer offset = myVideoWrap.getPageSize() * (myVideoWrap.getPageNum() - 1);
        if (videoListType == MYSELF_VIDEO_LIST) {
            videoList = myVideoWrap.getUid().equals(myVideoWrap.getLikeUid()) ? videoRepository.findByMyUids(myVideoWrap.getUid(), offset, myVideoWrap.getPageSize()) : videoRepository.findByOtherUids(myVideoWrap.getUid(), offset, myVideoWrap.getPageSize());
        } else if (videoListType == MY_CLICK_VIDEO_LIST) {
            List<String> clickVideoList = clickContentRepository.clickSum(myVideoWrap.getUid(), offset, myVideoWrap.getPageSize());
            HashMap<String, Video> collect = videoRepository.findByClickUid(clickVideoList).stream().collect(Collectors.toMap(Video::getVideoId, video -> video, (a, b) -> a, HashMap::new));
            for (String s : clickVideoList) {
                Optional.ofNullable(collect.get(s)).ifPresent(videoList::add);
            }
        }

        if (videoList.isEmpty()) {
            portalResponse.setCount(0);
            return portalResponse;
        }
        List<String> videoIds = new ArrayList<>(videoList.size());
        List<String> uidList = new ArrayList<>(videoList.size());
        Set<Long> addressIds = new HashSet<>();
        videoList.forEach(video -> Optional.ofNullable(video).ifPresent(v -> {
            videoIds.add(v.getVideoId());
            uidList.add(v.getUid());
            Optional.ofNullable(v.getAddressId()).ifPresent(addressIds::add);
        }));

        List<User> userList = userService.findAllByIds(uidList);
        Map<String, User> userMap =
                userList.stream().collect(Collectors.toMap(User::getUid, user -> user, (a, b) -> b));
        List<Address> addressList = addressService.findAllByIds(addressIds);
        Map<Long, Address> addressMap =
                addressList.stream().collect(Collectors.toMap(Address::getId, address -> address, (a, b) -> b));
        List<String> userIds = new ArrayList<>(userMap.keySet());
        Map<String, RelationInfo> relationMap =
                starFanService.queryRelation(userIds, userMap, myVideoWrap.getLikeUid());
        buildItems(portalResponse, videoIds, videoList, userMap, addressMap, myVideoWrap.getLikeUid(), relationMap, null);
        return portalResponse;
    }

    private Map<String, Boolean> getUserLikeMap(String uid, List<String> videoIds) {
        Map<String, Boolean> userLikeMap = new HashMap<>(4);
        if (StringUtils.isNotEmpty(uid)) {
            List<ClickContent> userLikeList = clickContentRepository.videoUserLike(videoIds, uid);
            if (!userLikeList.isEmpty()) {
                userLikeMap = userLikeList.stream().collect(Collectors.toMap(ClickContent::getContentId, clickContent -> true, (a, b) -> b, () -> new HashMap<>(4)));
            }
        }
        return userLikeMap;
    }
    

    @Override
    public VideoItem getDetail(String videoId, String uid) {
        Video video = videoRepository.getOne(videoId);
        VideoItem videoItem = new VideoItem();
        videoItem.setVideoId(videoId);
        User videoUser = userService.findById(video.getUid());
        videoItem.setAvatar(videoUser.getAvatar());
        videoItem.setCoverUrl(video.getCoverUrl());
        videoItem.setNickName(videoUser.getNickname());
        Address address = null;
        if (video.getAddressId() != null) {
            address = addressService.findAddressById(video.getAddressId());
        }
        videoItem.setPoiName(address != null ? address.getPoiName() : "周边");
        videoItem.setUid(video.getUid());
        videoItem.setSource(Video.SOURCE_USER);
        //        Integer like = clickContentRepository.videoLikeSum(videoId);
        Map<Integer, Integer> clickMap = get1ClickMap(videoId);
        videoItem.setLike(Optional.ofNullable(clickMap.get(0)).orElse(0));
        videoItem.setView(Optional.ofNullable(clickMap.get(1)).orElse(0));
        //        videoItem.setLike(like != null ? like : 0);
        videoItem.setIsUserLike(false);
        if (StringUtils.isNotEmpty(uid)) {
            ClickContent clickContent = clickContentRepository.videoUserLike(videoId, uid);
            if (clickContent != null) {
                videoItem.setIsUserLike(true);
            }
        }
        Optional.ofNullable(uid)
                .ifPresent(id -> videoItem.setRelationCode(starFanService.checkRelation(id, video.getUid())));
        videoItem.setWidth(video.getWidth());
        videoItem.setHeight(video.getHeight());
        videoItem.setDescription(video.getDescription());
        return videoItem;
    }

    private Map<Integer, Integer> get1ClickMap(String videoId) {
        List<Object> clickList = clickContentRepository.videoClickSum(videoId);
        Map<Integer, Integer> map = new HashMap<>(16);
        if (!clickList.isEmpty()) {
            clickList.forEach(object -> {
                Object[] ary = (Object[]) object;
                map.put(Integer.parseInt(ary[0].toString()), Integer.parseInt(ary[1].toString()));
            });
        }
        return map;
    }

    private AssumeRoleResponse assumeRole(
            String accessKeyId,
            String accessKeySecret,
            String roleArn,
            String roleSessionName,
            String policy,
            ProtocolType protocolType)
            throws com.aliyun.oss.ClientException, com.aliyuncs.exceptions.ClientException {
        // 创建一个 AliYun Acs Client，用于发起 OpenAPI 请求
        IClientProfile profile = DefaultProfile.getProfile(vodStsRegionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        // 创建一个 AssumeRoleRequest 并设置请求参数
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setVersion(STS_API_VERSION);
        request.setSysMethod(MethodType.POST);
        request.setSysProtocol(protocolType);
        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);
        request.setPolicy(policy);
        // 发起请求，并得到response
        return client.getAcsResponse(request);
    }

    @Override
    public AssumeRoleResponse getSts() {
        //noinspection unchecked
        AssumeRoleResponse response = (AssumeRoleResponse) redisService.get(STS_KEY);
        if (log.isDebugEnabled()) {
            log.debug("sts response:" + JSONObject.toJSONString(response));
        }
        return response;
    }

    @Override
    public AssumeRoleResponse setStsTiming() throws ClientException {
        AssumeRoleResponse response;
        // 只有RAM用户（子账号）才能调用 AssumeRole 接口
        // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
        // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
        // String accessKeyId = vodAccessKeyId;
        // String accessKeySecret = "pSxmMru1lkb5k7FTUduVwo6ddyb1xT";
        // AssumeRole API 请求参数: RoleArn, RoleSessionName, Policy, and DurationSeconds
        // RoleArn 需要在 RAM 控制台上获取
        // String roleArn = "acs:ram::1831423916153787:role/vodrole"; // 即角色详情页的Arn值
        // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
        // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
        // 具体规则请参考API文档中的格式要求
        // 自定义即可
        String roleSessionName = "lzxs";
        // 定制你的policy
        String policy = "{\n"
                + "  \"Version\": \"1\",\n"
                + "  \"Statement\": [\n"
                + "    {\n"
                + "      \"Action\": \"vod:*\",\n"
                + "      \"Resource\": \"*\",\n"
                + "      \"Effect\": \"Allow\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        // 此处必须为 HTTPS
        ProtocolType protocolType = ProtocolType.HTTPS;
        response = assumeRole(vodAccessKeyId, vodAccessKeySecret, vodRoleArn, roleSessionName, policy, protocolType);
        //noinspection unchecked
        redisService.set(STS_KEY, response, STS_EXPIRATION);
        return response;
    }

    @Override
    public String checkVideoStatus() {
        long delta = checkTimeout * 1000;
        Date startDate = new Date(System.currentTimeMillis() - delta);
        StringBuilder out = new StringBuilder();
        // 找出状态一直为上传成功的视频,但没有进一步处理的
        List<Video> videoList = videoRepository.findCheck(startDate);
        if (videoList.isEmpty()) {
            out.append("No video needs check");
        } else {
            // 先查转码是否成功
            out.append("checkVideoStatus:[");
            dealTranscodeStatus(videoList, false, out);
            dealAIAuditStatus(videoList, out);
            out.append("]\n");
        }
        //找出状态为审核通过，没有高宽的
        List<Video> videos = videoRepository.findWithoutDimension(startDate);
        if (videos.isEmpty()) {
            out.append("No video has null width");
        } else {
            out.append(" dealNullWidth:[");
            dealTranscodeStatus(videos, true, out);
            out.append("]\n");
        }
        return out.toString();
    }

    private void dealTranscodeStatus(List<Video> videoList, boolean fixDimension, StringBuilder out) {
        int num = videoList.size() / 10 + 1;
        for (int i = 0; i < num; i++) {
            StringBuilder sb = new StringBuilder();
            Map<String, Video> map = new HashMap<>();
            for (int j = 0; j < 10; j++) {
                int pos = i * 10 + j;
                if (pos == videoList.size()) {
                    break;
                }
                Video video = videoList.get(pos);
                sb.append(video.getVideoId()).append(',');
                map.put(video.getVideoId(), video);
            }
            String videoIds = sb.substring(0, sb.length() - 1);
            try {
                GetTranscodeSummaryRequest request = new GetTranscodeSummaryRequest();
                request.setVideoIds(videoIds);
                GetTranscodeSummaryResponse response = defaultAcsClient.getAcsResponse(request);
                List<GetTranscodeSummaryResponse.TranscodeSummary> summaryList = response.getTranscodeSummaryList();
                if (summaryList != null && summaryList.size() > 0) {
                    for (GetTranscodeSummaryResponse.TranscodeSummary transcodeSummary : summaryList) {
                        Video video = map.get(transcodeSummary.getVideoId());
                        if (log.isDebugEnabled()) {
                            log.debug("videoId:" + video + " TranscodeStatus:" + transcodeSummary.getTranscodeStatus());
                        }
                        if (!transcodeSummary.getTranscodeStatus().equals("CompleteAllSucc")) {
                            video.setStatus(STATUS_CUT_FAIL);
                            out.append("{videoId:")
                                    .append(video.getVideoId())
                                    .append(",status:")
                                    .append(STATUS_CUT_FAIL)
                                    .append("},");
                            videoRepository.save(video);
                        } else {
                            if (fixDimension) {
                                List<GetTranscodeSummaryResponse.TranscodeSummary.TranscodeJobInfoSummary>
                                        list = transcodeSummary.getTranscodeJobInfoSummaryList();
                                if (list != null && list.size() > 0) {
                                    GetTranscodeSummaryResponse.TranscodeSummary.TranscodeJobInfoSummary
                                            one = list.get(0);
                                    String width = one.getWidth();
                                    String height = one.getHeight();
                                    video.setWidth(Long.parseLong(width));
                                    video.setHeight(Long.parseLong(height));
                                    out.append("{videoId:")
                                            .append(video.getVideoId())
                                            .append(",width:")
                                            .append(width)
                                            .append(",height:")
                                            .append(height)
                                            .append("},");
                                    videoRepository.save(video);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("dealTranscodeStatus:", e);
            }
        }
    }

    private void dealAIAuditStatus(List<Video> videoList, StringBuilder sb) {
        for (Video video : videoList) {
            try {
                GetMediaAuditResultRequest request = new GetMediaAuditResultRequest();
                request.setMediaId(video.getVideoId());
                GetMediaAuditResultResponse response = defaultAcsClient.getAcsResponse(request);
                String res = response.getMediaAuditResult().getSuggestion();
                video.setAutoCheckTime(LocalDateTime.now());
                if (PASS.equals(res)) {
                    video.setStatus(STATUS_AUDIT_COMPLETE).setStatusMachine(MY_CHECK_PASS);
                    sb.append(sb.append("{videoId:").append(video.getVideoId()))
                            .append(",status:")
                            .append(STATUS_AUDIT_COMPLETE)
                            .append("},");
                } else if (REVIEW.equals(res)) {
                    video.setStatus(STATUS_AUDIT_REVIEW).setStatusMachine(MY_CHECK_REVIEW);
                    sb.append(sb.append("{videoId:").append(video.getVideoId()))
                            .append(",status:")
                            .append(STATUS_AUDIT_REVIEW)
                            .append("},");
                } else {
                    video.setStatus(STATUS_AUDIT_FAIL).setStatusMachine(MY_CHECK_BLOCK);
                    sb.append(sb.append("{videoId:").append(video.getVideoId()))
                            .append(",status:")
                            .append(STATUS_AUDIT_FAIL)
                            .append("},");
                }
                videoRepository.save(video);
            } catch (Exception e) {
                sb.append("{videoId:")
                        .append(video.getVideoId())
                        .append(",error:")
                        .append(e.getMessage())
                        .append("},");
            }
        }
    }

    @Override
    public void shuffleOpenVideos(String frontIds) {
        long delta = openDay * 24L * 3600 * 1000;
        Date startDate = new Date(System.currentTimeMillis() - delta);
        List<Video> openVideoList = videoRepository.findOpenLimit(startDate);
        Date now = new Date();
        Random random = new Random();
        List<Video> frontVideos = new ArrayList<>();
        if (!openVideoList.isEmpty()) {
            openVideoList.forEach(video -> {
                if (frontIds != null && frontIds.contains(video.getVideoId())) {
                    frontVideos.add(video);
                } else {
                    video.setCreateTime(new Date(now.getTime() - random.nextInt(SHUFFLE_PERIOD)));
                    if (log.isDebugEnabled()) {
                        log.debug("open video[" + video.getVideoId() + "] is refreshed to " + video.getCreateTime());
                    }
                }
            });
        }
        if (frontIds != null) {
            String[] ids = frontIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                for (Video video : frontVideos) {
                    if (video.getVideoId().equals(ids[i])) {
                        video.setCreateTime(new Date(now.getTime() + (ids.length - i) * 1000));
                        if (log.isDebugEnabled()) {
                            log.debug("open video[" + video.getVideoId() + "] is front to " + video.getCreateTime());
                        }
                        break;
                    }
                }
            }
        }
        videoRepository.saveAll(Objects.requireNonNull(openVideoList));
    }

    /**
     * @param event 回调响应JSON对象
     * @return int
     * @date Created on 15:24 2019/10/19 Author: Tablo.
     * <p>Description:[获取视频来源，若回调数据不包含EXTEND，则当作未知来源处理]
     */
    private int getSource(JSONObject event) {
        String jsonVideoFrom = event.getString(EXTEND);
        return StringUtils.isBlank(jsonVideoFrom)
                ? UNKNOWN_SOURCE
                : JSON.parseObject(jsonVideoFrom).getInteger(VIDEO_FROM_FIELD);
    }

    /**
     * @param event 回调响应JSON对象
     * @param video 本地未更新视频数据
     * @date Created on 15:25 2019/10/19 Author: Tablo.
     * <p>Description:[对回调所属事件进行校验并执行]
     */
    private void matchExecution(JSONObject event, Video video) {
        if (video.getStatus() == STATUS_DELETED) {
            log.debug("video已被下架，不进行处理" + video);
            return;
        }
        log.debug("video，getOne数据" + video);
        String eventType = event.getString(EVENT_TYPE);
        switch (eventType) {
            case STREAM_TRANSCODE_COMPLETE:
                streamTranscode(event, video);
                break;
            case FILE_UPLOAD_COMPLETE:
                video = fileUpload(event, video);
                break;
            case SNAPSHOT_COMPLETE:
                snapshot(event, video);
                break;
            case AI_MEDIA_AUDIT_COMPLETE:
                video = autoReview(event, video);
                break;
            case CREATE_AUDIT_COMPLETE:
                video = manualReview(event, video);
                break;
            default:
        }
        videoRepository.save(video);
    }

    /**
     * Created on 15:15 2019/11/04 Author: Tablo.
     *
     * <p>Description:[单视频转码]
     *
     * @param event 回调响应
     * @param video 本地未更新视频数据
     */
    private void streamTranscode(JSONObject event, Video video) {
        if (event.getString(VOD_STATUS_FIELD).equals(VOD_STATUS_SUCCESS)) {
            video.setWidth(event.getLongValue(WIDTH)).setHeight(event.getLongValue(HEIGHT));
        }
    }

    /**
     * Created on 15:20 2019/10/19 Author: Tablo.
     *
     * <p>Description:[文件上传]
     *
     * @param event 回调响应
     * @param video 本地未更新视频数据
     * @return com.quantumtime.qc.entity.feeds.Video
     */
    private Video fileUpload(JSONObject event, Video video) {
        return video.setStatus(STATUS_UPLOADED).setStorageLocation(event.getString(FILE_URL));
    }

    /**
     * Created on 15:20 2019/10/19 Author: Tablo.
     *
     * <p>Description:[视频截图]
     *
     * @param event 回调响应
     * @param video 本地未更新视频数据
     */
    private void snapshot(JSONObject event, Video video) {
        if (VOD_STATUS_SUCCESS.equals(event.getString(VOD_STATUS_FIELD))) {
            //如果是用户上传的封面图，不用再设置
            if (StringUtils.isNotEmpty(video.getCoverImageId())) {
                return;
            }
            Optional.ofNullable(event.getJSONArray(SNAPSHOTS))
                    .ifPresent(snapshots -> video.setCoverUrl(snapshots.get(0).toString()));
        } else {
            // 截图失败是因为转码失败
            video.setStatus(STATUS_CUT_FAIL);
        }
    }

    /**
     * Created on 11:39 2019/10/19 Author: Tablo.
     *
     * <p>Description:[处理人工审核]
     *
     * @param event 人工审核事件返回数据
     * @param video 本地未更新视频数据
     * @return com.quantumtime.qc.entity.feeds.Video
     */
    private Video manualReview(JSONObject event, Video video) {
        String auditStatus = event.getString(AUDIT_STATUS);
        return NORMAL.equals(auditStatus)
                ? video.setStatus(STATUS_AUDIT_COMPLETE).setStatusPerson(MY_CHECK_PASS)
                : video.setStatus(STATUS_AUDIT_FAIL).setStatusPerson(MY_CHECK_BLOCK);
    }

    /**
     * Created on 11:39 2019/10/19 Author: Tablo.
     *
     * <p>Description:[AI审核判定]
     *
     * @param event 审核返回数据
     * @param video 本地未更新视频数据
     */
    private Video autoReview(JSONObject event, Video video) {
        String result = event.getJSONObject(AI_DATA).getString(SUGGESTION);
        video.setAutoCheckTime(LocalDateTime.now());
        return PASS.equals(result)
                ? video.setStatus(STATUS_AUDIT_COMPLETE).setStatusMachine(MY_CHECK_PASS)
                : REVIEW.equals(result)
                ? video.setStatus(STATUS_AUDIT_REVIEW).setStatusMachine(MY_CHECK_REVIEW)
                : video.setStatus(STATUS_AUDIT_FAIL).setStatusMachine(MY_CHECK_BLOCK);
    }

    @Override
    public void reportVideo(ReportVideoRequest reportVideoRequest) throws Exception {
        ReportVideo reportVideo = reportVideoRepository
                .findExist(reportVideoRequest.getVideoId(), reportVideoRequest.getReportUid());
        if (reportVideo != null) {
            throw new Exception("已经举报了！");
        }
        reportVideo = new ReportVideo();
        reportVideo.setUid(reportVideoRequest.getUid());
        reportVideo.setVideoId(reportVideoRequest.getVideoId());
        reportVideo.setReportUid(reportVideoRequest.getReportUid());
        reportVideo.setReason(reportVideoRequest.getReason());
        reportVideo.setStatus(ReportVideo.STATUS_NOT_PROCESSED);
        reportVideo.setCreateTime(new Date());
        reportVideoRepository.save(reportVideo);
    }

    @Override
    public VideoItem findHottestVideo(String currentUid, Long activityId) {
        Video hottest = videoRepository.findOneHottest(currentUid, activityId);
        if (hottest == null) {
            return null;
        }
        Map<String, User> userMap = Collections.singletonMap(currentUid, userService.findById(currentUid));
        Long addressId = hottest.getAddressId();
        Map<Long, Address> addressMap = Collections.singletonMap(addressId, addressService.findAddressById(addressId));
        List<String> videoId = Collections.singletonList(hottest.getVideoId());
        Map<String, Integer> likeMap = clickContentService.getClickMap(videoId, true);
        Map<String, Integer> viewMap = clickContentService.getClickMap(videoId, false);
        Map<String, Boolean> userLikeMap = getUserLikeMap(currentUid, videoId);
        return createVideoItem(addressMap, userMap, likeMap, viewMap, userLikeMap, hottest, Collections.emptyMap(), null);
    }

    @Override
    public List<VideoItem> findActivityVideos(String currentUid, Long activityId, Integer pageNum, Integer size) {
        Pageable pageable = PageRequest.of(pageNum - 1, size, new Sort(Sort.Direction.DESC, "heat"));
        List<Video> videoList = videoRepository.queryActVideo(activityId, pageable);
        if (videoList.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> videoIds = videoList.stream().map(Video::getVideoId).collect(Collectors.toList());
        Set<Long> addressIds = videoList.stream().map(Video::getAddressId).collect(Collectors.toSet());
        Set<String> userIds = videoList.stream().map(Video::getUid).collect(Collectors.toSet());
        Map<String, User> userMap =
                userService.findAllByIds(userIds).stream().collect(Collectors.toMap(User::getUid, user -> user, (a, b) -> b));
        Map<String, Integer> likeMap = clickContentService.getClickMap(videoIds, true);
        Map<String, Integer> viewMap = clickContentService.getClickMap(videoIds, false);
        Map<String, Boolean> userLikeMap = getUserLikeMap(currentUid, videoIds);
        Map<Long, Address> addressMap = addressService.findAllByIds(addressIds).stream()
                .collect(Collectors.toMap(Address::getId, address -> address, (a, b) -> b));

        Map<String, RelationInfo> relationMap =
                starFanService.queryRelation(new ArrayList<>(userIds), userMap, currentUid);
        List<VideoItem> videoItems = new ArrayList<>();
        videoList.stream().filter(Objects::nonNull).forEach(video -> Optional.ofNullable(
                        createVideoItem(addressMap, userMap, likeMap, viewMap, userLikeMap, video, relationMap, null))
                .ifPresent(videoItems::add));
        int length = videoItems.size();
        int base = (pageNum - 1) * size + 1;
        IntStream.range(0, length).forEach(i -> videoItems.get(i).setHeatRanking(i + base));
        return videoItems;
    }

    @Override
    public void shareVideo(String videoId) {
        videoRepository.findById(videoId).ifPresent(this::accept);
    }

    private void accept(Video video) {
        videoRepository.save(video.setShares(Optional.ofNullable(video.getShares()).orElse(0) + 1));
    }

    @Data
    class VideoHandler {
        private PortalRequest portalRequest;
        private List<VideoIDSrouce> recommendList;
        private int startPos;
        private int endPos;
        private List<String> userVideoIds;
        private List<Video> videoList;
        private Map<String, User> userMap;
        private Map<Long, Address> addressMap;
        private Map<String, RelationInfo> relationMap;

        public VideoHandler(PortalRequest portalRequest, List<VideoIDSrouce> recommendList, int startPos, int endPos) {
            this.portalRequest = portalRequest;
            this.recommendList = recommendList;
            this.startPos = startPos;
            this.endPos = endPos;
        }

        public VideoHandler invoke() {
            List<VideoIDSrouce> page = recommendList.subList(startPos, endPos);
            userVideoIds = new ArrayList<>();
            page.forEach(videoIdSource -> userVideoIds.add(videoIdSource.getVideoID()));

            // 获取用户视频
            videoList = videoRepository.findAllById(userVideoIds);
            // 恢复顺序
            videoList = recover(videoList, userVideoIds);
            Set<String> uidList = new HashSet<>();
            Set<Long> addressIds = new HashSet<>();
            for (Video video : videoList) {
                uidList.add(video.getUid());
                if (video.getAddressId() != null) {
                    addressIds.add(video.getAddressId());
                }
            }
            List<User> userList = userService.findAllByIds(uidList);
            userMap = new HashMap<>(16);
            userList.forEach(user -> userMap.put(user.getUid(), user));
            List<Address> addressList = addressService.findAllByIds(addressIds);
            addressMap = new HashMap<>(16);
            addressList.forEach(address -> addressMap.put(address.getId(), address));
            List<String> userIds = userList.stream().map(User::getUid).collect(Collectors.toList());
            relationMap = starFanService.queryRelation(userIds, userMap, portalRequest.getUid());
            return this;
        }

        private List<Video> recover(List<Video> videoList, List<String> videoIds) {
            List<Video> res = new ArrayList<>(videoList.size());
            videoIds.forEach(id -> videoList.stream()
                    .filter(video -> id.equals(video.getVideoId()))
                    .findFirst()
                    .ifPresent(res::add));
            return res;
        }
    }
}
