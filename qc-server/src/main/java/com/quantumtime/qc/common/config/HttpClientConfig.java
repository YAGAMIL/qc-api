package com.quantumtime.qc.common.config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HttpClientConfig {

    @Value("${http.client.connect-timeout}")
    private int connectTimeout;

    @Value("${http.client.connection-request-timeout}")
    private int connectionRequestTimeout;

    @Value("${http.client.socket-timeout}")
    private int socketTimeout;

    private static final String charset = "UTF-8";

    @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig  requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        return HttpClientBuilder.create()
                .setRetryHandler(new StandardHttpRequestRetryHandler())
                .setDefaultRequestConfig(requestConfig).build();
    }
   /* public  String sendGetRequset(StringBuilder url ,String params){
        HttpGet httpGet = new HttpGet(url + params);
        //创建http实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        String result = "";
        try {
            response = httpClient.execute(httpGet);		//执行请求
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);				//确保它被完全消耗
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {

            }
        }
        if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){

        }
        return result;
    }*/
}
