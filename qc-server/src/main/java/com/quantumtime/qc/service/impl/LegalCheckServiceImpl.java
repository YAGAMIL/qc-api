package com.quantumtime.qc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.RoaAcsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.green.model.v20180509.TextScanResponse;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.quantumtime.qc.common.properties.AliCloudProperties;
import static com.quantumtime.qc.constant.VodEventConstant.PASS;
import com.quantumtime.qc.service.LegalCheckService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Resource;
import static org.apache.commons.codec.CharEncoding.UTF_8;
import org.apache.dubbo.config.annotation.Service;

/**
 * .Description: Program:qc-api.Created on 2019-10-31 09:41
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
public class LegalCheckServiceImpl implements LegalCheckService {

    @Resource AliCloudProperties aliCloudProperties;
    /**
     * Created on 9:40 2019/10/31 Author: Tablo.
     *
     * <p>Description:[校验文本合法]
     *
     * @param text 文本内容
     * @return java.lang.String
     */
    @Override
    public String checkText(String text) {
        return legal(makeTextRequest(text)) ? text : null;
    }

    /**
     * Created on 15:03 2019/11/08 Author: Tablo.
     *
     * <p>Description:[生成阿里云文本审核请求]
     *
     * @param text 待检查的文本内容
     * @return com.aliyuncs.green.model.v20180509.TextScanRequest
     */
    private TextScanRequest makeTextRequest(String text) {
        TextScanRequest textScanRequest = new TextScanRequest();
        // 指定api返回格式
        textScanRequest.setSysAcceptFormat(FormatType.JSON);
        textScanRequest.setHttpContentType(FormatType.JSON);
        // 指定请求方法
        textScanRequest.setSysMethod(MethodType.POST);
        textScanRequest.setSysEncoding(UTF_8);
        textScanRequest.setSysRegionId(REGION_CN_HANGZHOU);
        List<Map<String, Object>> tasks = new ArrayList<>();
        Map<String, Object> task = new LinkedHashMap<>();
        task.put(DATA_ID, UUID.randomUUID().toString());
        /*
         待检测的文本，长度不超过10000个字符
        */
        task.put(CONTENT, text);
        tasks.add(task);
        JSONObject data = new JSONObject();

        /*
         检测场景，文本垃圾检测传递：antispam
        */
        data.put(SCENES, Collections.singletonList(ANTISPAM));
        data.put(TASKS, tasks);
        textScanRequest.setHttpContent(data.toJSONString().getBytes(StandardCharsets.UTF_8), UTF_8, FormatType.JSON);
        // 请务必设置超时时间
        textScanRequest.setSysConnectTimeout(3000);
        textScanRequest.setSysReadTimeout(6000);
        return textScanRequest;
    }

    /**
     * Created on 15:02 2019/11/08 Author: Tablo.
     *
     * <p>Description:[通过阿里SDK检验合法性]
     *
     * @param textScanRequest 文本审核请求体
     * @return boolean
     */
    private boolean legal(RoaAcsRequest<TextScanResponse> textScanRequest) {
        HttpResponse httpResponse = null;
        try {
            httpResponse = client().doAction(textScanRequest);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        AtomicReference<String> suggestion = new AtomicReference<>();
        if (Objects.requireNonNull(httpResponse).isSuccess()) {
            JSONObject scrResponse =
                    JSON.parseObject(new String(httpResponse.getHttpContent(), StandardCharsets.UTF_8));
            if (scrResponse.getInteger(CODE) == HTTP_SUCCESS) {
                scrResponse.getJSONArray(DATA).stream()
                        .filter(taskResult -> HTTP_SUCCESS == ((JSONObject) taskResult).getInteger(CODE))
                        .forEach(taskResult -> ((JSONObject) taskResult).getJSONArray(RESULTS).forEach(sceneResult ->
                                suggestion.set(((JSONObject) sceneResult).getString(TEXT_SUGGESTION))));
            }
        }
        return suggestion.get().equals(PASS);
    }

    private IAcsClient client() {
        return new DefaultAcsClient(DefaultProfile.getProfile(
                REGION_CN_HANGZHOU, aliCloudProperties.getAccessKey(), aliCloudProperties.getSecret()));
    }
}
