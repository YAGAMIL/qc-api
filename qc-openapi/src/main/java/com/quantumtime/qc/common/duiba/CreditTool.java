package com.quantumtime.qc.common.duiba;

import com.quantumtime.qc.common.duiba.entity.CreditNotifyParams;
import com.quantumtime.qc.common.exception.ExpFunction;

import java.util.HashMap;
import java.util.Map;

import static com.quantumtime.qc.common.DuiBaProperties.APP_KEY;
import static com.quantumtime.qc.common.DuiBaProperties.APP_SECRET;

public class CreditTool {

    private String appKey;
    private String appSecret;

    public CreditTool(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }


    /**
     * 通用的url生成方法
     *
     * @param url URL
     * @param params 参数
     * @return 最终URL
     */
    public String buildUrlWithSign(String url, Map<String, String> params) {
        Map<String, String> newParams = new HashMap<>(params);
        newParams.put("appKey", appKey);
        newParams.put("appSecret", appSecret);
        if (newParams.get("timestamp") == null) {
            newParams.put("timestamp", System.currentTimeMillis() + "");
        }
        String sign = SignTool.sign(newParams);
        newParams.put("sign", sign);

        newParams.remove("appSecret");

        return AssembleTool.assembleUrl(url, newParams);
    }


    public static void parseCreditNotify(
            CreditNotifyParams params, String appSecret) {
        Map<String, String> map = params.toRequestMap(APP_SECRET);
        ExpFunction.true4Throw(!params.getAppKey().equals(APP_KEY), new RuntimeException("appKey不匹配"));
        ExpFunction.true4Throw(params.getTimestamp() == null, new RuntimeException("请求中没有带时间戳"));
        ExpFunction.true4Throw(!SignTool.signVerify(appSecret, map), new RuntimeException("签名验证失败"));
    }
}
