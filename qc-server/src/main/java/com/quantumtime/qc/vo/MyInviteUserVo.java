package com.quantumtime.qc.vo;

import com.quantumtime.qc.entity.Invite;
import lombok.Data;

@Data
public class MyInviteUserVo extends Invite {
    private String nickName;
    private String avatar;
    private long createTimeLong;

}
