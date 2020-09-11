package com.quantumtime.qc.vo.recommend;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class RecommendResult {
    private int count;
    @JSONField(name="current_count")
    private int currentCount;
    @JSONField(name="recommender_list")
    private List<Recommend> recommendList;
    @JSONField(name="page_num")
    private int pageNum;
    @JSONField(name="page_size")
    private int pageSize;
}
