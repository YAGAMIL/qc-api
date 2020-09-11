package com.quantumtime.qc.vo.wechat;

import com.quantumtime.qc.vo.recommend.Poi;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FeedVo {
    private long feeds_id;

    private String create_time;
    private String ext_param;
    private List<Poi> pois;

}
