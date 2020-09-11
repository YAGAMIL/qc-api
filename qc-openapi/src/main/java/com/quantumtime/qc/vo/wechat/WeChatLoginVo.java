package com.quantumtime.qc.vo.wechat;

import lombok.Data;

@Data
public class WeChatLoginVo {

    private String phoneNumber;

    //手机号加密偏移量
    private String phoneNumberIv;

    private String jsCode;

    private String userInfo;

    private String userInfoIv;

}
