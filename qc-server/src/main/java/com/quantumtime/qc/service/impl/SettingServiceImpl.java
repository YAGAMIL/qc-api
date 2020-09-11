package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.entity.SettingEntity;
import com.quantumtime.qc.common.service.impl.BaseServiceImpl;
import com.quantumtime.qc.repository.SettingRepository;
import com.quantumtime.qc.service.ISettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SettingServiceImpl extends BaseServiceImpl<SettingEntity, String, SettingRepository> implements ISettingService{

    @Cacheable(value = "cache-setting", key = "'code'+#s", unless="#result == null")
    @Override
    public SettingEntity findById(String s) {
        return super.findById(s);
    }
}
