package com.quantumtime.qc.vo.video;

import lombok.Data;

import java.util.List;

@Data
public class PortalResponse {
    //当前页返回条数
    private int count;
    //总的还剩条数
    private int rest;
    private List<VideoItem> videoList;

}
