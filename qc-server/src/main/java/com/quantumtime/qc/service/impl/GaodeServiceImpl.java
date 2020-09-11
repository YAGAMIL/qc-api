package com.quantumtime.qc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.service.IGaodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GaodeServiceImpl implements IGaodeService {

    @Value("${gaode.wsUrl}")
    private String wsUrl;

    @Value("${gaode.key}")
    private String key;

    @Value("${gaode.types}")
    private String types;

    @Value("${gaode.radius}")
    private int radius;

    @Value("${gaode.offset}")
    private int offset;

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private IAddressService addressService;

    @Override
    public List<Long> getNearCommunities(String longitude, String latitude) {
        long start = System.currentTimeMillis();
        List<Long> result = null;
        CloseableHttpResponse response = null;
        try {
            check(longitude, latitude);
            longitude = adjustPrecision(longitude);
            latitude = adjustPrecision(latitude);

            HttpGet httpGet = new HttpGet(wsUrl + "?key=" + key
                    + "&location=" + longitude + "," + latitude + "&radius=" + radius
                    + "&types=" + types + "&offset=" + offset + "&sortrule=distance");
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = response.getEntity();
                String content = EntityUtils.toString(httpEntity, "UTF-8");
                if (log.isDebugEnabled()) {
                    log.debug("gaode near response:" + content);
                }
                JSONObject jsonObject = JSON.parseObject(content);
                String status = jsonObject.getString("status");
                if (!"1".equals(status)) {
                    log.error("gaode error:" + content);
                    throw new Exception("访问高德失败");
                }
                JSONArray pois = jsonObject.getJSONArray("pois");
                if (pois != null && pois.size() > 0) {
                    List<String> poiIds = new ArrayList<>(pois.size());
                    for (int i = 0; i < pois.size(); i++) {
                        JSONObject element = pois.getJSONObject(i);
                        poiIds.add(element.getString("id"));
                    }
                    result = addressService.findAllByPoIds(poiIds);
                }
            }
            log.info("got address num:" + result.size() + " getNearCommunities consumes time:"
                    + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            log.error("getNearCommunities error", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error(" error:", e);
                }
            }
        }
        return result;
    }

    private String adjustPrecision(String digit) throws Exception {
        //保留6位小数
        int pos = digit.indexOf('.');
        if (pos <= 0) {
            throw new Exception("经纬度格式不对:" + digit);
        }
        String beforePart = digit.substring(0, pos);
        String afterPart = digit.substring(pos + 1);
        if (afterPart.length() > 6) {
            afterPart = afterPart.substring(0, 7);
        }
        return beforePart + "." + afterPart;
    }

    private void check(String longitude, String latitude) throws Exception {
        if (StringUtils.isEmpty(longitude)) {
            throw new Exception("经度为空");
        }
        if (StringUtils.isEmpty(latitude)) {
            throw new Exception("纬度为空");
        }
    }
}
