package com.quantumtime.qc.vo.recommend;

import lombok.Data;

import java.util.Date;

@Data
public class FeedSummary {

    private String avatar;
    private String nickname;
    private String characterUrl;
    private String feedType;
    private int like;
    private int view;
    private String city;
    private String community;
    private Date createTime;
    private Boolean userLike;
}
