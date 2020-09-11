package com.quantumtime.qc.common.constant;

import com.quantumtime.qc.common.utils.VerifyUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Description:登路常量
 * Program:qc-api
 * </p>
 * Created on 2019-10-14 17:21
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

public class LoginConstant {
    /**
     * 默认UnionId
     */
    private static final String DEFAULT_UNION_ID = "0";

    /**
     * Author: Tablo
     * <p> Description:[判定微信Id，返回true则为空，false则为有值]
     * Created on 17:26 2019/10/14
     *
     * @param unionId 微信标识
     * @return boolean
     **/
    public static boolean isLegalUnionId(String unionId) {
        return StringUtils.isNotBlank(unionId) && !DEFAULT_UNION_ID.equals(unionId);
    }

    /**
     * Author: Tablo
     * <p> Description:[判定手机号，返回true则为正常，false则为手机号有问题]
     * Created on 10:56 2019/10/15
     *
     * @param phone 手机号
     * @return boolean
     **/
    public static boolean isLegalPhone(String phone) {
        return StringUtils.isNotBlank(phone) && !VerifyUtil.notPhone(phone);
    }
}