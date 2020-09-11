package com.quantumtime.qc.vo.recommend;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PoiInfo {
    @JSONField(name="poi_id")
    private String poiId;
    @JSONField(name="poi_name")
    private String poiName;
    @JSONField(name="poi_type")
    private String poiType;
}
