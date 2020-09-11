package com.quantumtime.qc.entity.score;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:积分相关基类 Program:qc-api Created on 2019-12-09 15:17
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public class BaseScoreModel implements Serializable {

    /** 发起 */
    public static final int LAUNCH = 0;

    /** 已完成 */
    public static final int COMPLETED = 1;

    /** 失败 */
    public static final int FAIL = 2;

    /** 积分记录类型-任务 */
    public static final int TASK = 0;

    /** 积分记录类型-提现 */
    public static final int WITHDRAW = 1;

    /** 积分记录类型-兑换 */
    public static final int EXCHANGE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** 用户id */
    @Column(name = "uid")
    private String uid;

    /** 消耗积分 */
    @Column(name = "score")
    private Long score;
    /** 提现状态，0:发起，1已完成，2：失败 */
    @Column(name = "status")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
