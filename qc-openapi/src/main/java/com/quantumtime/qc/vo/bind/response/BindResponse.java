package com.quantumtime.qc.vo.bind.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * Description:用户绑定返回信息
 * Program:saas-server
 * </p>
 * Created on 2019-09-04 20:03
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */


@ApiModel(description = "BindResponse,响应结果")
@Data
@Accessors(chain = true)
public class BindResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("绑定结果,0已绑定，1未绑定")
    Integer weChatIsBind = 1;

    @ApiModelProperty("1=不是（手机号未注册），0=是（手机号码已注册）")
    Integer phoneIsReg = 1;

    @ApiModelProperty("绑定成功后自动登录并返回token，绑定失败则不返回")
    String token;
}
