package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.entity.ErrorCodeEntity;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.repository.ErrorCodeRepository;
import com.quantumtime.qc.service.IErrorCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ErrorCodeServiceImpl extends BaseServiceImpl<ErrorCodeEntity, String, ErrorCodeRepository> implements IErrorCodeService {

    @Cacheable(value = "cache-errorCode", key = "'code'+#s", unless="#result == null")
    @Override
    public ErrorCodeEntity findById(String s) {
        return super.findById(s);
    }

    @Override
    public ErrorCodeEntity getErrorCodeEntityByErrorCode(String errorCode) {
        return ((IErrorCodeService) AopContext.currentProxy()).findById(errorCode);
    }
}
