package com.quantumtime.qc.vo.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Description: 携带Token的登录类
 * Created on 2019/09/03 15:55
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LoginUserVo extends BaseLoginVo {

    private static final long serialVersionUID = 1L;
    /**
     * Token
     */
    @ApiModelProperty("用户token")
    private String token;

    /**
     * anonymousId
     */
    @ApiModelProperty("设备anonymousId")
    private String anonymousId;

}