package com.quantumtime.qc.vo.recommend;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Poi {
    @JSONField(name="poi_id")
    @JsonProperty("poi_id")
    private String poiId;
    @JSONField(name="poi_name")
    @JsonProperty("poi_name")
    private String poiName;
    @JSONField(name="poi_type")
    @JsonProperty("poi_type")
    private String poiType;
}
