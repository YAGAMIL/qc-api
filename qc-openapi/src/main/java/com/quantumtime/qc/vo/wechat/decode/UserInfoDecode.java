package com.quantumtime.qc.vo.wechat.decode;

import lombok.Data;
/**
 * .Description: Token用户decode & Created on 2019/10/28 11:55
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
public class UserInfoDecode {

    private String openId;

    private String nickName;

    private String gender;

    private String city;

    private String province;

    private String country;

    private String avatarUrl;

    private String unionId;
}
