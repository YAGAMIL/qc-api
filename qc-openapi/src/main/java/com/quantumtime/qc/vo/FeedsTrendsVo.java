package com.quantumtime.qc.vo;

import com.quantumtime.qc.vo.wechat.FeedVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FeedsTrendsVo {

    /**
     * 动态id
     */
    private String author_id;

    /**
     * 创建时间
     */
    private String author_name;

    /**
     * 保留字段
     */
    private List<FeedVo> thrends;



}
