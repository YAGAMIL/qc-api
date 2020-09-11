package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.entity.Version;
import com.quantumtime.qc.repository.VersionRepository;
import com.quantumtime.qc.service.VersionService;
import com.quantumtime.qc.wrap.VersionWarp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VersionServiceImlp implements VersionService {
    @Autowired
    VersionRepository versionRepository;
    public  Version updateVersion(VersionWarp versionWarp){
        Version maxVersion = versionRepository.newVersion();
        if (maxVersion.getVersionCode()==null||maxVersion.getVersionCode().longValue() == versionWarp.getVersionCode().longValue()) {
            return null;
        }

        return maxVersion;
    }
     public String downloadUrl(){
         Version maxVersionCode = versionRepository.newVersion();
         String url=maxVersionCode.getDownUrl();
         return url;
     }
}
