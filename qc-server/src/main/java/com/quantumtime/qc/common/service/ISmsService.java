package com.quantumtime.qc.common.service;

import com.aliyuncs.CommonResponse;

import java.util.Map;

/**
 * 短信服务接口
 */
public interface ISmsService {

    /**
     * 发送短信
     *
     * @param phoneNumbers
     * @param signName
     * @param templateCode
     * @param templateParams
     * @return
     */
    CommonResponse sendSms(String phoneNumbers, String signName, String templateCode, Map templateParams);

    /**
     * Author: Tablo
     * <p> Description:[生成6位随机验证码]
     * Created on 15:27 2019/09/03
     *
     * @return java.lang.String
     **/
    String generateCaptcha();

}
