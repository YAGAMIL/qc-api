package com.quantumtime.qc.service;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.quantumtime.qc.vo.video.PortalRequest;
import com.quantumtime.qc.vo.video.PortalResponse;
import com.quantumtime.qc.vo.video.ReportVideoRequest;
import com.quantumtime.qc.vo.video.UploadImageAddressRequest;
import com.quantumtime.qc.vo.video.UploadVideoAddressRequest;
import com.quantumtime.qc.vo.video.VideoItem;
import com.quantumtime.qc.wrap.video.MyVideoWrap;

import java.util.List;

public interface IVideoService {
  CreateUploadVideoResponse uploadVideoAddress(UploadVideoAddressRequest uploadVideoAddressRequest)
      throws ClientException;

  RefreshUploadVideoResponse refreshUploadVideoAddress(String videoId) throws ClientException;

  CreateUploadImageResponse uploadImageAddress(UploadImageAddressRequest uploadImageAddressRequest)
      throws Exception;

  GetVideoPlayAuthResponse playVideoAuth(String backendIdentity, String videoId)
      throws ClientException;

  void deleteVideo(String videoIds, String backendIdentity) throws Exception;

  void callback(String body);

  PortalResponse getPortalResponse(PortalRequest portalRequest) throws Exception;

  /**
   * Getter for property 'sts'.
   *
   * @return Value for property 'sts'.
   */
  AssumeRoleResponse getSts() throws ClientException;

  PortalResponse personalVideoList(MyVideoWrap myVideoWrap);

  PortalResponse personaLikeVideoList(MyVideoWrap myVideoWrap);

  VideoItem getDetail(String videoId, String uid);

  GetPlayInfoResponse playVideoInfo(String videoId) throws ClientException;

  AssumeRoleResponse setStsTiming() throws ClientException;

  String checkVideoStatus();

  /**
   * 清洗上传视频，并置顶指定的视频
   *
   * @param frontIds 置顶的videoIds
   */
  void shuffleOpenVideos(String frontIds);

  PortalResponse getAddressVideoList(PortalRequest portalRequest);

  void reportVideo(ReportVideoRequest reportVideoRequest) throws Exception;
  /**
   * Author: Tablo
   *
   * <p>Description:[获取用户某活动最高热度的视频] Created on 14:29 2019/12/14
   *
   * @param currentUid 用户id
   * @param activityId 活动id
   * @return com.quantumtime.qc.vo.video.VideoItem
   */
  VideoItem findHottestVideo(String currentUid, Long activityId);

  /**
   * Author: Tablo
   *
   * <p>Description:[获取所有参与活动的视频] Created on 17:58 2019/12/14
   *
   * @param currentUid 当前用户
   * @param activityId 活动id
   * @param pageNum 页码
   * @param size 大小
   * @return java.util.Map<java.lang.Integer,com.quantumtime.qc.vo.video.VideoItem>
   */
  List<VideoItem> findActivityVideos(
      String currentUid, Long activityId, Integer pageNum, Integer size);

  void shareVideo(String videoId);
}
