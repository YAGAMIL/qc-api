package com.quantumtime.qc.vo;

import com.quantumtime.qc.entity.feeds.Feeds;
import com.quantumtime.qc.vo.recommend.Poi;
import lombok.Data;

import java.util.List;

@Data
public class MqFeeds {
      Feeds feeds;
      String phone;
     List<Poi> poi;
}
