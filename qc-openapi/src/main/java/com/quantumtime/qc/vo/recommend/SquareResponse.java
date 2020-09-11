package com.quantumtime.qc.vo.recommend;

import lombok.Data;

import java.util.List;

@Data
public class SquareResponse {

    private int pageNum;

    private int pageSize;

    private int count;

    private int currentCount;

    private List<FeedSummary> feedsList;

}
