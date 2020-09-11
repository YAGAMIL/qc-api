package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.entity.Config;
import com.quantumtime.qc.repository.ConfigRepository;
import com.quantumtime.qc.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ConfigServiceImlp implements ConfigService {
    @Autowired
    ConfigRepository configRepository;

    @Override
    public List<Config> all() {
        return configRepository.findAll();
    }
}
