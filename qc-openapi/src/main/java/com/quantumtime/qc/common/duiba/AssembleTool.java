package com.quantumtime.qc.common.duiba;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
public class AssembleTool {

  public static final String QUESTION_MARK = "?";
    public static String assembleUrl(String url, Map<String, String> params) {
        if (!url.endsWith(QUESTION_MARK)) {
            url += QUESTION_MARK;
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        params.forEach((k, v) ->
                urlBuilder
                        .append(k)
                        .append("=")
                        .append(v == null || v.length() == 0 ? v + "&" : encode(v))
                        .append("&"));
        url = urlBuilder.toString();
        return url;
    }

    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Encode执行失败，内容为" + value);
        }
        return value;
    }
}
