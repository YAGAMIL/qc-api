package com.quantumtime.qc.vo.amap.response;

import lombok.Data;

import java.util.List;

@Data
public class PlaceAroundResponse {

    private String status;

    private String count;

    private String info;

    private String infocode;

    private List<PlaceAroundPOIDetailResponse> pois;


}
