package com.quantumtime.qc.service.impl.activity;

import com.quantumtime.qc.entity.activity.HeatRule;
import com.quantumtime.qc.repository.HeatRuleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Description: Program:qc-api Created on 2019-12-18 11:54
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
public class HeatRuleServiceImpl implements HeatRuleService {
    @Resource
    private HeatRuleRepository heatRuleRepository;

    @Override
    public Optional<HeatRule> matchActRul(Long activityId) {
        return heatRuleRepository.findById(activityId);
    }
}
