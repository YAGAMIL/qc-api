package com.quantumtime.qc.common.duiba.result;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:兑吧通知返回结果 Created on 2019/12/09 17:37
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
public class CreditConsumeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String errorMessage = "";
    private String bizId = "";
    private Long credits = -1L;

    public CreditConsumeResult(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        if (success) {
            return "{'status':'ok','errorMessage':'','bizId':'" + bizId + "','credits':'" + credits + "'}";
        } else {
            return "{'status':'fail','errorMessage':'" + errorMessage + "','credits':'" + credits + "'}";
        }
    }
}
