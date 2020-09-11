package com.quantumtime.qc.wrap.video;

import lombok.Data;

import java.util.Map;

@Data
public class MyVideoWrap {
  private Integer pageNum;
  private Integer pageSize;
  private String uid ;
  private String likeUid;
}
