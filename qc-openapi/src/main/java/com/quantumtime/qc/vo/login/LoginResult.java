package com.quantumtime.qc.vo.login;

import com.quantumtime.qc.vo.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Description:注册返回类 Created on 2019/09/09 13:09
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "登陆/注册返回结果")
public class LoginResult implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 用户信息 */
    @ApiModelProperty("用户信息")
    private UserInfo userInfo;

    /** 登陆后的token值 */
    @ApiModelProperty("用户token")
    private String token;
}
