package com.quantumtime.qc.entity.score;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:提现实体 Created on 2019/12/03 16:34
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "withdraw")
public class Withdraw implements Serializable {

    public static final long MIN = 30000;

    public static final long MEDIUM = 100000;

    public static final long MAX = 300000;

    public static final int MULTIPLE = 10000;

    public static final String DECIMAL_2 = "0.00";
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户id */
    @Column(name = "uid")
    private String uid;

    /** 兑换金额 */
    @Column(name = "amount")
    private String amount;

    /** 消耗积分 */
    @Column(name = "score")
    private Long score;

    /** 手机号 */
    @Column(name = "phone")
    private String phone;

    /** 提现方式：0-支付宝，1-微信 */
    @Column(name = "type")
    private Integer type;

    /** 用户昵称 */
    @Column(name = "nick_name")
    private String nickName;

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
