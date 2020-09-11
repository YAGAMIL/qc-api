package com.quantumtime.qc.vo.bind.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * Description:手机号是否注册与小区信息是否填写
 * Program:saas-server
 * </p>
 * Created on 2019-09-05 11:58
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

@ApiModel(description = "对应参数是否注册与小区信息是否填写")
@Data
@Accessors(chain = true)
public class AddrParamHas implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 手机号是否注册：0=已注册，1=未注册
     */
    @ApiModelProperty(notes = "参数类型值是否注册：0=已注册，1=未注册")
    private Integer paramIsReg = 1;

    /**
     * 用户是否有地址：0=已填写，1=未填写
     */
    @ApiModelProperty(notes = "用户是否有地址：0=已填写，1=未填写")
    private Integer hasAddr = 1;

}
