package com.quantumtime.qc.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 短信服务VO
 */
@ApiModel("短信服务VO")
@Data
public class SmsVo {

    @ApiParam(value = "手机号", required = true)
    @NotEmpty
    private String phoneNumber;

    @ApiParam(value = "发送类型 0为注册验证码 1为登录验证码 2为修改手机号", required = true)
    @NotEmpty
    private String action;

}
