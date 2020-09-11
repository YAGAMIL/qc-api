package com.quantumtime.qc.common.duiba;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SignTool {

    public static final String SIGN_FIELD = "sign";
    public static final String APP_SECRET_FIELD = "appSecret";


    public static boolean signVerify(String appSecret, Map<String, String> params) {
        Map<String, String> map = new HashMap<>(8);
        map.put(APP_SECRET_FIELD, appSecret);
        params.keySet().stream().filter(key -> !SIGN_FIELD.equals(key)).forEach(key -> map.put(key, params.get(key)));
        String sign = sign(map);
        return sign.equals(params.get(SIGN_FIELD));
    }

    private static String toHexValue(byte[] messageDigest) {
        if (messageDigest == null) {
			return "";
		}
        StringBuilder hexValue = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            int val = 0xFF & aMessageDigest;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    /**
     * @param params
     * @return
     */
    public static String sign(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String string = keys.stream().map(params::get).collect(Collectors.joining());
		String sign = "";
        try {
            sign = toHexValue(encryptMD5(string.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("md5 error");
        }
        return sign;
    }

    private static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

    public static void main(String[] args) {
        String appKey = "key";
        String appSecret = "secret";

        Map<String, String> params = new HashMap<>(8);
        params.put("appKey", appKey);
        params.put(APP_SECRET_FIELD, appSecret);
        params.put("date", new Date().getTime() + "");

        String sign = sign(params);

        params.put("sign", sign);

        System.out.println(signVerify(appSecret, params));
    }
}
