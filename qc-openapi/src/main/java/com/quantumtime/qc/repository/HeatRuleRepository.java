package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.activity.HeatRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Description:热度规则持久层
 * Program:qc-api
 * </p>
 * Created on 2019-12-17 18:51
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Repository
public interface HeatRuleRepository  extends JpaRepository<HeatRule, Long> {
    
}
