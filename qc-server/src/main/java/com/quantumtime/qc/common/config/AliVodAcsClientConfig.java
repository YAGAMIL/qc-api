package com.quantumtime.qc.common.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliVodAcsClientConfig {

    @Value("${aliyun.vodAccessKeyId}")
    private String vodAccessKeyId;

    @Value("${aliyun.vodAccessKeySecret}")
    private String vodAccessKeySecret;

    @Value("${aliyun.vodRegionId}")
    private String vodRegionId;

    @Bean
    public DefaultAcsClient defaultAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(vodRegionId,
                vodAccessKeyId, vodAccessKeySecret);
        return new DefaultAcsClient(profile);
    }
}
