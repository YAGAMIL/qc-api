package com.quantumtime.qc.service;

import com.quantumtime.qc.common.duiba.entity.CreditConsumeParams;
import com.quantumtime.qc.common.duiba.entity.CreditNotifyParams;
import com.quantumtime.qc.vo.DuiBaVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description: Program:qc-api Created on 2019-12-09 11:59
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface DuiBaService {

    /**
     * Author: Tablo
     *
     * <p>Description:[] Created on 18:46 2019/12/09
     *
     * @param params 请求参数
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean verifyResults(CreditNotifyParams params);


    DuiBaVo integralUpdate(CreditConsumeParams params);

}
