package com.quantumtime.qc.service.impl;

import com.alibaba.fastjson.JSON;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.ITlsService;
import com.quantumtime.qc.vo.tls.TlsModifyAccount;
import com.quantumtime.qc.vo.tls.TlsRegistAccount;
import com.quantumtime.qc.vo.tls.response.TlsResponse;
import com.tls.tls_sigature.tls_sigature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.zip.DataFormatException;

@Service
@Slf4j
public class TlsServiceImpl implements ITlsService {

    @Autowired
    private AccountHelp accountHelp;

    @Value("${tlsSig.privStr}")
    private String privStr;

    @Value("${tlsSig.pubStr}")
    private String pubStr;

    @Value("${tlsSig.apkAppId}")
    private String apkAppId;

    @Value("${tlsSig.admin}")
    private String admin;

    @Value("${tlsSig.registTlsUserUrl}")
    private String registTlsUserUrl;

    @Value("${tlsSig.modifyTlsUserUrl}")
    private String modifyTlsUserUrl;

    @Autowired
    private RestTemplate restTemplate;

    protected String generateRequestUrl(String url){
        int i = new Random().nextInt();
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append("?")
            .append("sdkappid=").append(apkAppId)
            .append("&identifier=").append(admin)
            .append("&usersig=").append(this.genTLSSignatureEx(admin).urlSig)
            .append("&random=").append(i*i)
            .append("&contenttype=json");
        return sb.toString();
    }

    @Override
    public tls_sigature.GenTLSSignatureResult genTLSSignatureEx() {
        User currentUser = accountHelp.getCurrentUser();
        tls_sigature.GenTLSSignatureResult result = this.genTLSSignatureEx(currentUser.getPhone());
        return result;
    }

    @Override
    public tls_sigature.GenTLSSignatureResult genTLSSignatureEx(String identifier) {
        tls_sigature.GenTLSSignatureResult result = tls_sigature.GenTLSSignatureEx(Long.parseLong(apkAppId), identifier, privStr);
        return result;
    }

    @Override
    public tls_sigature.CheckTLSSignatureResult checkTLSSignatureEx(String urlSig) throws DataFormatException {
        User currentUser = accountHelp.getCurrentUser();
        tls_sigature.CheckTLSSignatureResult checkResult = tls_sigature.CheckTLSSignatureEx(urlSig, Long.parseLong(apkAppId), currentUser.getPhone(), pubStr);
        return checkResult;
    }

    @Override
    public boolean registTlsUser(TlsRegistAccount account) {
        try {
            String url = generateRequestUrl(registTlsUserUrl);
            //设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            //请求体
            HttpEntity<String> formEntity = new HttpEntity<String>(JSON.toJSONString(account), headers);
            //发起请求
            String jsonResult = restTemplate.postForObject(url, formEntity, String.class);
            TlsResponse tlsResponse = JSON.parseObject(jsonResult, TlsResponse.class);
            if (tlsResponse.getErrorCode() != null && tlsResponse.getErrorCode() != 0) {
                throw new RuntimeException("创建腾讯IM账户失败 TlsAccount: " +  account.toString() + "TlsResponse: " + tlsResponse.toString(), new Throwable());
            }
            return true;
        }catch (Exception e){
            throw new RuntimeException("创建腾讯IM账户失败 TlsAccount: " + account.toString(), new Throwable());
        }
    }

    @Override
    public boolean modifyTlsUser(TlsModifyAccount account) {
        try {
            String url = generateRequestUrl(modifyTlsUserUrl);
            //设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            //请求体
            HttpEntity<String> formEntity = new HttpEntity<String>(JSON.toJSONString(account), headers);
            //发起请求
            String jsonResult = restTemplate.postForObject(url, formEntity, String.class);
            TlsResponse tlsResponse = JSON.parseObject(jsonResult, TlsResponse.class);
            if (tlsResponse.getErrorCode() != null && tlsResponse.getErrorCode() != 0) {
                throw new RuntimeException("修改腾讯IM账户失败 TlsModifyAccount: " +  account.toString() + "TlsResponse: " + tlsResponse.toString(), new Throwable());
            }
            return true;
        }catch (Exception e){
            throw new RuntimeException("修改腾讯IM账户失败 TlsModifyAccount: " + account.toString(), new Throwable());
        }
    }
}
