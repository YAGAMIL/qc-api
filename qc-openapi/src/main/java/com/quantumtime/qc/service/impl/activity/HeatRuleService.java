package com.quantumtime.qc.service.impl.activity;

import com.quantumtime.qc.entity.activity.HeatRule;

import java.util.Optional;

/**
 * <p>
 * Description:活动热度业务层设计
 * Program:qc-api
 * </p>
 * Created on 2019-12-18 11:53
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface HeatRuleService {
    Optional<HeatRule> matchActRul(Long activityId);
}
