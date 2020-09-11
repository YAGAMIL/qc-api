package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.Version;
import com.quantumtime.qc.wrap.VersionWarp;

public interface VersionService {
    /**
     * app更新版本
     *
     * @param
     * @return
     */
    Version updateVersion(VersionWarp versionWarp);
    String downloadUrl();
}
