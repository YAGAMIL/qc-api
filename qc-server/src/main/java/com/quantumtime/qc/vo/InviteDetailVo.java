package com.quantumtime.qc.vo;

import lombok.Data;

@Data
public class InviteDetailVo {
    //邀请码
    private Integer id;
    //是否可以领取奖励 1可以，0不可以.
    private int isReceive;
    //用户可用的积分
    private Long score;
    //可提现金额
    private Double money;
    //0第一次提现还在，1已经提现过了
    private int withdrawType;


}
