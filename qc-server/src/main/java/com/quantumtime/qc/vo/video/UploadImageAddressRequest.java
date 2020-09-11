package com.quantumtime.qc.vo.video;

import lombok.Data;

@Data
public class UploadImageAddressRequest {

    private String title;

    private String fileName;

    private String description;

    private String cateId;

    private String tags;

}
