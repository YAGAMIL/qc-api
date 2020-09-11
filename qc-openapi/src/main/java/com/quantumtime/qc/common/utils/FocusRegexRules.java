package com.quantumtime.qc.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description:
 * Program:saas-server
 * </p>
 * Created on 2019-10-11 19:38
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

public class FocusRegexRules {
    private static List<String> regexList;

    static {
        init();
    }

    static synchronized private void init() {
        if (regexList == null) {
            regexList = new ArrayList<>();
        } else {
            return;
        }
        regexList.add("^(0|13|15|18|168|400|800)[0-9]*$");
        regexList.add("^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$");
        regexList.add("^\\d*(1688|2688|2088|2008|5188|10010|10001|666|888|668|686|688|866|868|886|999)\\d*$");
        // 重复号码，镜子号码
        regexList.add("^(<a>\\d)(\\d)(\\d)\\1\\2\\3$");
        // AABB
        regexList.add("^\\d*(\\d)\\1(\\d)\\2\\d*$");
        // AAABBB
        regexList.add("^\\d*(\\d)\\1\\1(\\d)\\2\\2\\d*$");
        // ABABAB  
        regexList.add("^(\\d)(\\d)\\1\\2\\1\\2\\1\\2$");
        // ABCABC
        regexList.add("^(\\d)(\\d)(\\d)\\1\\2\\3$");
        // ABBABB
        regexList.add("^(\\d)(\\d)\\2\\1\\2\\2$");
        // AABAAB
        regexList.add("^(\\d)\\1(\\d)\\1\\1\\2$");
        // 4-8 位置重复
        regexList.add("^\\d*(\\d)\\1{2,}\\d*$");
        // 4位以上 位递增或者递减（7890也是递增）
        regexList.add("(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)|9(?=0)){2,}|(?:0(?=9)|9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){2,})\\d");
        // 不能以 518 、918 结尾
        regexList.add("^[0-9]*(518|918)$");
    }

    public boolean isAllow(String input) {
        return !Focus2IdUtil.contains(input, regexList);
    }

}
