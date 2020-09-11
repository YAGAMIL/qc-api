package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.Config;

import java.util.List;

public interface ConfigService {
    /**
     * 返回所以配置
     */
    List<Config> all();
}
