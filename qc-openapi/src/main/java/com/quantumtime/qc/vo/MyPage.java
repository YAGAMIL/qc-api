package com.quantumtime.qc.vo;

import lombok.Data;


@Data
public class MyPage {

    private int page = 0;

    private int size = 10;

    private String search = null;

    private Integer cid = null;

}
