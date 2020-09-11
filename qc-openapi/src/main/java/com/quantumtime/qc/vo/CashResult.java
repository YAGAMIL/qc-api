package com.quantumtime.qc.vo;

import com.google.common.base.Converter;
import com.quantumtime.qc.entity.score.Withdraw;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;

/**
 * Description:提现返回结果 Program:qc-api Created on 2019-12-03 17:03
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Accessors(chain = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CashResult {

    public static final String QUOTA_IS_ILLEGAL = "额度不合法";
    public static final String NOT_ENOUGH = "您的积分不足";
    public static final String ARRIVED_IN24_HOURS = "审核通过后，24小时内到账。";
    public static final String ARRIVED_IN72_HOURS = "审核通过后，72小时内到账。";

    private Integer id;

    /** 用户id */
    private String uid;

    /** 兑换金额 */
    private String amount;

    /** 消耗积分 */
    private Long score;

    /** 手机号 */
    private String phone;

    /** 提现方式：0-支付宝，1-微信 */
    private Integer type;

    /** 用户昵称 */
    private String nickName;

    /** 提现状态，0:发起，1已完成，2：失败 */
    private Integer status;

    private String msg;

    private boolean result;

    @SuppressWarnings("unused")
    public CashResult convert2Result(Withdraw withdraw) {
        return new InfoConvert().reverse().convert(withdraw);
    }

    @SuppressWarnings("unused")
    public Withdraw convert2With() {
        return new InfoConvert().convert(this);
    }

    /** The type Vo convert. */
    public static class InfoConvert extends Converter<CashResult, Withdraw> {

        @Override
        protected Withdraw doForward(@Nonnull CashResult user) {
            Withdraw withdraw = new Withdraw();
            BeanUtils.copyProperties(user, withdraw);
            return withdraw;
        }

        @Override
        protected CashResult doBackward(@Nonnull Withdraw withdraw) {
            CashResult user = new CashResult();
            BeanUtils.copyProperties(withdraw, user);
            return user;
        }
    }
}
