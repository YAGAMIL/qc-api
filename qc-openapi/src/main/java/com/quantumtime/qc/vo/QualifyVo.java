package com.quantumtime.qc.vo;

import lombok.Data;

@Data
public class QualifyVo {
    private String uid;

    private String mail;

    private String nickname;

    private String phone;

    private String wxNumber;

    private String projectUrl;

    private short authType = -1;

    private String memo;

    private String hobbies;

    private String qualifyUrl1;

    private String qualifyUrl2;

    private String qualifyUrl3;

    private String characterName;

    private String characterUrl;

    private Long createTime;
}
