package com.quantumtime.qc.vo.bind.response;

import com.quantumtime.qc.vo.login.LoginUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Description:绑定手机号请求体 Program:qc-api Created on 2019-12-04 16:58
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiModel(description = "微信绑定消息体")
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BindPhone extends LoginUserVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户uid")
    String uid;
}
