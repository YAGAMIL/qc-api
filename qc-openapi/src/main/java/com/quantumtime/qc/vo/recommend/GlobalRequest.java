package com.quantumtime.qc.vo.recommend;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class GlobalRequest {
    /**
     * 从1开始
     */
    @JSONField(name="page_num")
    private int pageNum;
    @JSONField(name="page_size")
    private int pageSize;
    @JSONField(name="poi_list")
    private List<Poi> poiList;
    @JSONField(name="user_id")
    private String userId;

}
