package com.quantumtime.qc.vo;

import lombok.Data;

import java.util.List;

@Data
public class MyFeedsSumVo {
    List<MyFeedsVo> FeedsList;
    Integer sum;
   

}
