package com.quantumtime.qc.constant;

/**
 * .Description:阿里视频处理事件枚举 Program:qc-api.Created on 2019-10-18 11:35
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface VodEventConstant {

    /** ----------------------云视频操作字段判定---------------------- File upload complete vod event enum. 返回状态字段 */
    @SuppressWarnings("unused")
    String EVENT_TYPE = "EventType",
            /** File upload complete vod event enum. 视频上传完成 */
            FILE_UPLOAD_COMPLETE = "FileUploadComplete",

            /** Image upload complete vod event enum. 图片上传完成 */
            IMAGE_UPLOAD_COMPLETE = "ImageUploadComplete",

            /** Stream transcode complete vod event enum. 单个清晰度转码完成 */
            STREAM_TRANSCODE_COMPLETE = "StreamTranscodeComplete",

            /** Snapshot complete vod event enum. 视频截图完成 */
            SNAPSHOT_COMPLETE = "SnapshotComplete",

            /** Create audit complete vod event enum. 人工审核完成 */
            CREATE_AUDIT_COMPLETE = "CreateAuditComplete",

            /** Ai media audit complete vod event enum. 智能审核完成 */
            AI_MEDIA_AUDIT_COMPLETE = "AIMediaAuditComplete",

            /** The Size.视频上传后回传的size字段，标识视频大小 */
            SIZE = "Size",

            /** The File url. 视频上传后回传的size字段，标识视频地址 */
            FILE_URL = "FileUrl",

            /** The Audit.作业结果数据。JSON对象，具体结构见 */
            AI_DATA = "Data",

            /** The Suggestion.AI返回结果字段 */
            SUGGESTION = "Suggestion",

            /** The Block.AI审核违规 */
            BLOCK = "block",

            /** The Review.AI审核疑似 */
            REVIEW = "review",

            /** The Pass.AI审核通过 */
            PASS = "pass",

            /** The Audit status.人工审核结果字段 */
            AUDIT_STATUS = "AuditStatus",

            /** 视频 Height. */
            HEIGHT = "Height",

            /** 视频 Width. */
            WIDTH = "Width",

            /** The Blocked.人工审核违规 */
            BLOCKED = "Blocked",

            /** The Normal.人工审核通过 */
            NORMAL = "Normal",

            /** ----------------------UPLOAD自定义---------------------- The Extend. 上传时的扩展字段 */
            EXTEND = "Extend",

            /** The Video from field. Extend子字段,协定好用来判断视频来源的标识 */
            VIDEO_FROM_FIELD = "videoFrom",

            /**
             * ---------------------回调响应------------------------- The Media resp.MediaId响应m，标识VideoId->"MediaId"的响应字段
             */
            MEDIA_RESP = "MediaId",

            /** The Video resp.VideoId响应，标识VideoId->"VideoId"的响应字段 */
            VIDEO_RESP = "VideoId",

            /** The Vod status. 回调状态字段 */
            VOD_STATUS_FIELD = "Status",

            /** The Vod status success.操作成功 */
            VOD_STATUS_SUCCESS = "success",

            /** The Vod status fail.操作失败 */
            VOD_STATUS_FAIL = "fail";

    /** The constant APP.来源为APP，上传会存入数据库，来源自App的视频，是videoFrom的value值 */
    @SuppressWarnings("unused")
    int APP = 0,
            /** The MIS.来源为运营后台，先不存数据库，The App video. 来源自App的视频，是videoFrom的value值 */
            MIS = 1,

            /** The Unknown. 未知来源 */
            UNKNOWN_SOURCE = -1;
}
