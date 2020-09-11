package com.quantumtime.qc.service;

import com.quantumtime.qc.vo.CashResult;

/**
 * Description:提现业务接口 Program:qc-api Created on 2019-12-03 17:01
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface WithdrawService {

    /**
     * Author: Tablo
     *
     * <p>Description:[提取现金] Created on 17:11 2019/12/03
     *
     * @param uid 用户id
     * @param consumeScore 消耗分数
     * @param type 提现方式，0支付宝，1微信
     * @return com.quantumtime.qc.vo.CashResult
     */
    CashResult cash(String uid, Long consumeScore, Integer type);
}
