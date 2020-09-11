package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.exception.ExpFunction;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.entity.score.ScoreLog;
import com.quantumtime.qc.entity.score.Withdraw;
import com.quantumtime.qc.repository.ScoreLogRepository;
import com.quantumtime.qc.repository.UserRepository;
import com.quantumtime.qc.repository.WithdrawRepository;
import com.quantumtime.qc.service.WithdrawService;
import com.quantumtime.qc.vo.CashResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.quantumtime.qc.common.constant.ErrorCodeConstant.ACCOUNT_PHONE_NOT_EXIST_REGISTER;
import static com.quantumtime.qc.entity.score.BaseScoreModel.LAUNCH;
import static com.quantumtime.qc.entity.score.BaseScoreModel.WITHDRAW;
import static com.quantumtime.qc.entity.score.Withdraw.DECIMAL_2;
import static com.quantumtime.qc.entity.score.Withdraw.MAX;
import static com.quantumtime.qc.entity.score.Withdraw.MEDIUM;
import static com.quantumtime.qc.entity.score.Withdraw.MIN;
import static com.quantumtime.qc.entity.score.Withdraw.MULTIPLE;
import static com.quantumtime.qc.vo.CashResult.ARRIVED_IN24_HOURS;
import static com.quantumtime.qc.vo.CashResult.ARRIVED_IN72_HOURS;
import static com.quantumtime.qc.vo.CashResult.NOT_ENOUGH;
import static com.quantumtime.qc.vo.CashResult.QUOTA_IS_ILLEGAL;

/**
 * Description: Program:qc-api Created on 2019-12-03 17:12
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
public class WithdrawServiceImpl implements WithdrawService {
    @Resource ScoreLogRepository scoreLogRepository;
    @Resource private UserRepository userRepository;
    @Resource private WithdrawRepository withdrawRepository;

    private static String decimal2Money(Long consumeScore) {
        return new DecimalFormat(DECIMAL_2).format(consumeScore / MULTIPLE);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CashResult cash(String uid, Long consumeScore, Integer type) {
        User user = userRepository.findUserByUid(uid);
        ExpFunction.true4ThrowBiz(user.getPhone() == null, ACCOUNT_PHONE_NOT_EXIST_REGISTER);
        Long score = Optional.ofNullable(user.getScore()).orElse(0L);
        CashResult fail = CashResult.builder().result(false).build();
        return !inCashLevel(consumeScore)
                ? fail.setMsg(QUOTA_IS_ILLEGAL)
                : score < consumeScore ? fail.setMsg(NOT_ENOUGH) : right(uid, user, consumeScore, type);
    }

    public CashResult right(String uid, User user, Long consumeScore, Integer type) {
        int first = withdrawRepository.findFirst(uid);
        Long score = Optional.ofNullable(user.getScore()).orElse(0L);
        Long scoreFreeze = Optional.ofNullable(user.getScoreFreeze()).orElse(0L);
        String money = decimal2Money(consumeScore);
        LocalDateTime now = LocalDateTime.now();
        Withdraw withdraw = withdrawRepository.save(
                buildWithdraw(money, user, consumeScore, type).setCreateTime(now).setUpdateTime(now));
        userRepository.save(user.setScore(score - consumeScore).setScoreFreeze(scoreFreeze + consumeScore));
        scoreLogRepository.save(buildScoreLog(uid, consumeScore, withdraw.getId()).setCreateTime(now));
        return new CashResult()
                .convert2Result(withdraw)
                .setMsg(first > 0 ? ARRIVED_IN72_HOURS : ARRIVED_IN24_HOURS)
                .setResult(true);
    }

    private boolean inCashLevel(Long consumeScore) {
        return consumeScore == MIN || consumeScore == MEDIUM || consumeScore == MAX;
    }

    private Withdraw buildWithdraw(String money, User user, Long consumeScore, Integer type) {
        return new Withdraw()
                .setAmount(money)
                .setPhone(user.getPhone())
                .setScore(consumeScore)
                .setStatus(LAUNCH)
                .setUid(user.getUid())
                .setType(type);
    }

    private ScoreLog buildScoreLog(String uid, Long consumeScore, Long recordId) {
        return new ScoreLog()
                .setUid(uid)
                .setScore(consumeScore)
                .setRecordId(recordId)
                .setTranType(WITHDRAW)
                .setDescription("提现")
                .setType(false);
    }
}
