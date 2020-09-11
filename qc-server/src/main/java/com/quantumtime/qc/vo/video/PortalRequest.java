package com.quantumtime.qc.vo.video;

import com.quantumtime.qc.entity.poi.Address;
import lombok.Data;

@Data
public class PortalRequest {
    private String requestId;
    private Address address;
    private Integer pageNum;
    private Integer pageSize;
    private String uid;
}
