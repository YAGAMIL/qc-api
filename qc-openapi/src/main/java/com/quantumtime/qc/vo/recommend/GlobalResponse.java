package com.quantumtime.qc.vo.recommend;

import lombok.Data;

@Data
public class GlobalResponse {
    private int code;
    private String message;
    private RecommendResult data;
}
