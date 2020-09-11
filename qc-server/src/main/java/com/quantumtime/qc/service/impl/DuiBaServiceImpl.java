package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.duiba.CreditTool;
import com.quantumtime.qc.common.duiba.entity.CreditConsumeParams;
import com.quantumtime.qc.common.duiba.entity.CreditNotifyParams;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.entity.score.Exchange;
import com.quantumtime.qc.entity.score.ScoreLog;
import com.quantumtime.qc.repository.ExchangeRepository;
import com.quantumtime.qc.repository.ScoreLogRepository;
import com.quantumtime.qc.service.DuiBaService;
import com.quantumtime.qc.service.IUserService;
import com.quantumtime.qc.vo.DuiBaVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.quantumtime.qc.common.DuiBaProperties.APP_SECRET;
import static com.quantumtime.qc.entity.score.BaseScoreModel.COMPLETED;
import static com.quantumtime.qc.entity.score.BaseScoreModel.FAIL;

/**
 * Description: Program:qc-api Created on 2019-12-09 12:01
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
public class DuiBaServiceImpl implements DuiBaService {

    private static final int TRAN_TYPE_DUIBA = 2;
    private static final int STATUS_START = 0;
    @Resource
    private ExchangeRepository exchangeRepository;
    @Resource
    private IUserService userService;
    @Resource
    private ScoreLogRepository scoreLogRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean verifyResults(CreditNotifyParams params) {
        CreditTool.parseCreditNotify(params, APP_SECRET);
        String orderNum = params.getOrderNum();
        Exchange exchange = exchangeRepository.findExchangeByOrderId(orderNum);
        String uid = exchange.getUid();
        LocalDateTime now = LocalDateTime.now();
        Long deducted = exchange.getScore();
        User consumer = userService.findById(uid);
        Long score = Optional.ofNullable(consumer.getScore()).orElse(0L);
        Long freezeScore = Optional.ofNullable(consumer.getScoreFreeze()).orElse(0L);
        // 兑换成功
        if (params.isSuccess()) {
            consumer.setScoreFreeze(freezeScore - deducted);
            exchange.setStatus(COMPLETED).setUpdateTime(now);
            // 兑换失败，根据orderNum，对用户的金币进行返还，回滚操作
        } else {
            consumer.setScore(score + deducted).setScoreFreeze(freezeScore - deducted);
            exchange.setStatus(FAIL).setUpdateTime(now);
        }
        userService.save(consumer);
        exchangeRepository.save(exchange);
        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public DuiBaVo integralUpdate(CreditConsumeParams params) {
        User user = userService.findById(params.getUid());
        Exchange exchangeSave = new Exchange()
                .setOrderId(params.getOrderNum())
                .setUid(params.getUid())
                .setScore(params.getCredits())
                .setPhone(user.getPhone())
                .setNickName(user.getNickname())
                .setCreateTime(LocalDateTime.now());
        Exchange exchange = exchangeRepository.save(exchangeSave);

        ScoreLog scoreLog = new ScoreLog()
                .setUid(params.getUid())
                .setScore(params.getCredits())
                .setType(false)
                .setTranType(TRAN_TYPE_DUIBA)
                .setDescription(params.getDescription())
                .setCreateTime(LocalDateTime.now())
                .setRecordId(exchange.getId());
        scoreLogRepository.save(scoreLog);

        Long score = Optional.ofNullable(user.getScore()).orElse(0L);
        DuiBaVo duiBaVo = new DuiBaVo();
        if (score < params.getCredits()) {
            duiBaVo.setSurplus(0L);
            return duiBaVo;
        }
        user.setScore(score - params.getCredits());
        user.setScoreFreeze(params.getCredits());
        User newUser = userService.save(user);


        duiBaVo.setBizId(exchange.getId());
        duiBaVo.setSurplus(newUser.getScore());

        return duiBaVo;
    }
}
