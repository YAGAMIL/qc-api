package com.quantumtime.qc.common.utils;

import java.util.regex.Pattern;

/**
 * Description: 校验工具类
 * Created on 2019/09/09 13:46
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public class VerifyUtil {
    public static boolean notPhone(String phone) {
        String regex = "^((1))\\d{10}$";
        if (phone.length() != 11) {
            return true;
        } else {
            return !Pattern.compile(regex).matcher(phone).matches();
        }
    }

}
