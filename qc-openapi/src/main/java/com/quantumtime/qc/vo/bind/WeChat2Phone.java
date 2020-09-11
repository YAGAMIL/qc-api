package com.quantumtime.qc.vo.bind;

import com.quantumtime.qc.vo.login.LoginUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * Description:
 * Program:saas-server
 * </p>
 * Created on 2019-09-04 18:10
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

@ApiModel(description = "微信绑定消息体")
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class WeChat2Phone extends LoginUserVo {

    private static final long serialVersionUID = 1L;
    /**
     * 微信全平台下的唯一标识
     */
    @ApiModelProperty("微信unionId（全平台唯一）")
    String unionId;
}
