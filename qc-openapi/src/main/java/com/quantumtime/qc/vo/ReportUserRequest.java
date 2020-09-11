package com.quantumtime.qc.vo;

import lombok.Data;

@Data
public class ReportUserRequest {
    private String uid;
    private String reportUid;
    private String reason;
}
