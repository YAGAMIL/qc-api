package com.quantumtime.qc.vo.video;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VideoItem {

    private String videoId;
    private String coverUrl;
    private Integer source;
    private String playUrl;
    private Long width;
    private Long height;
    private String uid;
    private String avatar;
    private Integer gender;
    private String poiName;
    private String nickName;
    private Integer like;
    private Integer view;
    private Boolean isUserLike;
    private Integer status;
    /**该视频用户的生活圈id*/
    private Long userAddressId;
    private String description;
    private int relationCode;
    private Boolean isLandOwner;
    private Long heat;

    /** 经度 */
    private String longitude;

    /** 纬度 */
    private String latitude;

    private  int heatRanking;
}
