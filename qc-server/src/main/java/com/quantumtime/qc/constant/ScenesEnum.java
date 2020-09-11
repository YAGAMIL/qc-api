package com.quantumtime.qc.constant;

/**
 * .Description:场景枚举 Program:qc-api.Created on 2019-11-07 11:53
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

public enum ScenesEnum {

    /** Antispam scenes enum. 文本场景——邮件*/
    ANTISPAM("antispam"),

    /** Porn scenes enum. 图片场景——涉黄*/
    PORN("porn"),

    /** Ad scenes enum. 图片场景——广告，图文违规识别*/
    AD("ad"),

    /** Logo scenes enum. 图片场景——图片logo识别*/
    LOGO("logo"),

    /** Live scenes enum. 图片场景——不良场景识别*/
    LIVE("live"),

    /** Qrcode scenes enum. 图片场景——图片二维码识别*/
    QRCODE("qrcode"),

    /** Terrorism scenes enum. 图片场景——图片暴恐涉政识别*/
    TERRORISM("terrorism");

    ScenesEnum(String scenes) {
    }
}
