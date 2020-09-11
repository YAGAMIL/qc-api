package com.quantumtime.qc.vo.video;

import lombok.Data;

@Data
public class ReportVideoRequest {
    private String uid;
    private String videoId;
    private String reportUid;
    private String reason;
}
