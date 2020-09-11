package com.quantumtime.qc.vo.video;

import com.quantumtime.qc.entity.poi.Address;
import lombok.Data;

@Data
public class UploadVideoAddressRequest {

    private String uid;

    private String title;

    private String fileName;

    private String fileSize;

    private String description;

    private String coverUrl;

    private String coverImageId;

    private String cateId;

    private String tags;

    private Address shotPlace;

    private Address scopeAddress;

    private Integer source;

    private String backendIdentity;

    private Long activityId;

    private String activityName;
}
