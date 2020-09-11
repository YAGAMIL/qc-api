package com.quantumtime.qc.vo.recommend;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Recommend {
    @JSONField(name="feeds_type")
    private String feedsType;
    @JSONField(name="feeds_id")
    private long feedsId;
    @JSONField(name="author_id")
    private String authorId;
    @JSONField(name="author_name")
    private String authorName;
    @JSONField(name="create_time")
    private String createTime;
    @JSONField(name="ext_param")
    private String extParam;
    @JSONField(name="click_info")
    private ClickInfo clickInfo;
    @JSONField(name="poi_info")
    private PoiInfo poiInfo;
}
