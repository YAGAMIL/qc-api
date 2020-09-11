package com.quantumtime.qc.service;

import com.quantumtime.qc.common.entity.ErrorCodeEntity;
import com.quantumtime.qc.common.service.IBaseService;

public interface IErrorCodeService extends IBaseService<ErrorCodeEntity, String> {


    /**
     * 获取errorcode
     * @param errorCode
     * @return
     */
    ErrorCodeEntity getErrorCodeEntityByErrorCode(String errorCode);



}
