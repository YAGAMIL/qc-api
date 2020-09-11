package com.quantumtime.qc.vo.bind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * Description:微信端绑定手机
 * Program:saas-server
 * </p>
 * Created on 2019-09-03 15:50
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiModel(description = "UnionId,微信标识")
@Data
@Accessors(chain = true)
public class UnionId implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 微信全平台下的唯一标识
     */
    @ApiModelProperty("微信unionId（全平台唯一）")
    String unionId;
}