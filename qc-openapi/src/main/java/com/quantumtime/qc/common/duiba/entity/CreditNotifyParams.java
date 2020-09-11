package com.quantumtime.qc.common.duiba.entity;

import com.quantumtime.qc.common.duiba.SignTool;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 兑换通知参数 Created on 2019/12/09 16:54
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class CreditNotifyParams {

    private boolean success;
    private String bizId = "";
    private String errorMessage = "";
    private String orderNum = "";
    private Date timestamp = new Date();
    private String appKey;
    private String uid = "";

    public Map<String, String> toRequestMap(String appSecret) {
        Map<String, String> map = new HashMap<>(16);
        map.put("success", success + "");
        map.put("errorMessage", getString(errorMessage));
        map.put("bizId", getString(bizId));
        map.put("appKey", getString(appKey));
        map.put("appSecret", getString(appSecret));
        map.put("timestamp", getString(timestamp.getTime()));
        map.put("uid", getString(uid));
        map.put("orderNum", getString(orderNum));

        String sign = SignTool.sign(map);

        map.remove("appSecret");
        map.put("sign", sign);
        return map;
    }

    private String getString(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }
}
