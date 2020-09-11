package com.quantumtime.qc.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MyFeedsVo {
    private Long feedsId;
    private String content;
    private String picture;
    private String addressName;
    private String city;
    private Date createTime;
    private Long like;
    private Long view;
    private Boolean UserLike;
    private String feedsAvatar;
    private String nickName;

}
