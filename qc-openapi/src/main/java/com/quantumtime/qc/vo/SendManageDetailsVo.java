package com.quantumtime.qc.vo;

import com.quantumtime.qc.entity.undo.SendManage;
import com.quantumtime.qc.entity.User;
import lombok.Data;

@Data
public class SendManageDetailsVo {

    private String base64QrCode;

    private User user;

    private SendManage sendManage;

}
