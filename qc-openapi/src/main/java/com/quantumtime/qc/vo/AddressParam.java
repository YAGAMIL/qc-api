package com.quantumtime.qc.vo;

import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.poi.Business;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * Description:全地域属性类
 * Program:qc-api
 * </p>
 * Created on 2019-10-10 17:09
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "地址信息所需全部参数")
public class AddressParam implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 所属商圈
     */
    @ApiModelProperty("所属商圈")
    private List<Business> businessList;

    /**
     * 所属地址
     */
    @ApiModelProperty("所属商圈")
    private Address address;
}