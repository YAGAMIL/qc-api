/*
package com.quantumtime.qc.jobhandler;

import com.alibaba.fastjson.JSON;
import com.quantumtime.qc.common.config.HttpClientConfig;
import com.quantumtime.qc.service.IRedisService;
import com.quantumtime.qc.service.IVideoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Map;

@JobHandler(value="wchatJSApiHandler")
@Component
public class WchatJSApiHandler extends IJobHandler{
    @Autowired
    private IVideoService videoService;
    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private HttpClientConfig httpClientConfig;
    @Value("${WeChat.loginUrl}")
    private String loginUrl;
    @Value("${WeChat.appid}")
    private String appid;
    @Value("${WeChat.secret}")
    private String secret;
    @Value("${WeChat.grant_type}")
    private String grantType;


        @Override
        public ReturnT<String> execute(String param) throws Exception {
            String ticket=updateTicket(getAccessToken());
            XxlJobLogger.log("ticket,value."+ JSON.toJSONString(ticket));
            return SUCCESS;
        }
    private String getAccessToken() {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(loginUrl).append("?").append("grant_type=").append(grantType).append("&appid=").append(appid).append("&secret=").append(secret);
        String res = httpClientConfig.sendGetRequset(requestUrl, "");
        Map<String, Object> resMap = (Map<String, Object>) JSON.parse(res);
        return (String)resMap.get("access_token");


    }
    private String updateTicket(String accessToken) throws Exception {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(loginUrl).append("ticket").append("getticket?access_token=").append(accessToken);
        String res = httpClientConfig.sendGetRequset(requestUrl, "");
        Map<String, Object> resMap = (Map<String, Object>) JSON.parse(res);
        String ticket = (String)resMap.get("ticket");
        if (StringUtils.isEmpty(ticket)) {
            throw new Exception("jsapi_ticket为空");
        }
        redisService.set("ticket",ticket);
        return ticket;
    }

}
*/
