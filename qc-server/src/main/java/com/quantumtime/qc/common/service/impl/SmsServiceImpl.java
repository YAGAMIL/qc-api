package com.quantumtime.qc.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.quantumtime.qc.common.service.ISmsService;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * .Description: 短信发送业务实现 & Created on 2019/11/05 14:15
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Slf4j
@Service
public class SmsServiceImpl implements ISmsService {

    @Value("${aliyun.accessKey}")
    private String accessKeyId;

    @Value("${aliyun.secret}")
    private String secret;

    @Value("${aliyun.smsRegisterCode}")
    private String smsRegisterCode;

    @Value("${aliyun.smsLoginCode}")
    private String smsLoginCode;

    @Value("${aliyun.signName}")
    private String signName;

    @Override
    public CommonResponse sendSms(String phoneNumbers, String signName, String templateCode, Map templateParams) {
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", JSON.toJSONString(templateParams));
        CommonResponse response = new CommonResponse();
        try {
            response = client.getCommonResponse(request);
            log.debug(response.getData());
            return response;
        } catch (ClientException e) {
            log.error("发送短信失败", e);
        }
        response.setHttpStatus(500);
        response.setData("发送短信异常");
        return response;
    }

    @Override
    public String generateCaptcha() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }
}
