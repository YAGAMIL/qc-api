package com.quantumtime.qc.common.duiba.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AddCreditsParams {

    private String appKey;
    /** 时间戳 */
    private Date timestamp;
    /** 增加积分值 */
    private Long credits;
    /** 兑吧订单号 */
    private String orderNum = "";

    private String description = "";
    /** 积分活类型，活动和签到 */
    private String type = "";

    private String uid = "";
    /** 用户兑换时使用的ip地址，有可能为空 */
    private String ip = "";
    /** 非必须参数 */
    private String transfer = "";
}
