package com.quantumtime.qc.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * .Description:阿里云服务配置类 Program:qc-api.Created on 2019-11-05 11:15
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliCloudProperties {

    private String accessKey;
    private String secret;
    private String smsRegisterCode;
    private String smsLoginCode;
    private String smsModifyPhoneCode;
    private String signName;
    private String pushIosAppKey;
    private String pushAndroidAppKey;
    private String vodAccessKeyId;
    private String vodAccessKeySecret;
    private String vodRoleArn;
    private String vodStsRegionId;
    private String vodRegionId;
    private String vodWorkFlowId;
    private String vodCallbackKey;
}
