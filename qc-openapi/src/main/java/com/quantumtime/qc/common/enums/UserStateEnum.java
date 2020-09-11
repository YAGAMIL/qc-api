package com.quantumtime.qc.common.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:用户状态枚举 Created on 2019/10/15 20:34
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum UserStateEnum {

    /** Disable user state enum.0.禁用 */
    DISABLE(0, "账号已被禁用"),
    /** Enable user state enum.1.正常 */
    ENABLE(1, "账号正常"),
    /** Delete user state enum. 2.注销 */
    DELETE(2, "账号已被注销"),
    /** Unverified first user state enum.3.已注册未完善用户信息地址 */
    UNVERIFIED_FIRST(3, "已注册未验证用户信息地址第一步"),
    /** Unverified second user state enum.4.已注册未通过定位验证第一步 */
    UNVERIFIED_SECOND(4, "已注册未验证用户信息地址第二步"),
    /** Unverified third user state enum.5.已注册未通过定位验证第二步 */
    UNVERIFIED_THIRD(5, "已注册未验证用户信息地址第三步");

    @Setter private Integer code;

    @Setter private String state;

    /**
     * Get enum by code user state enum.
     *
     * @param code the code
     * @return the user state enum
     */
    public static UserStateEnum getEnumByCode(Integer code) {
        return Arrays.stream(values())
                .filter(userStateEnum -> userStateEnum.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
