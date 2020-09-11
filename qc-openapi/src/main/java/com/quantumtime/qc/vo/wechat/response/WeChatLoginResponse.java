package com.quantumtime.qc.vo.wechat.response;

import lombok.Data;

@Data
public class WeChatLoginResponse {

    private String openid;

    private String session_key;

    private Integer errcode;

    private String err_msg;

}
