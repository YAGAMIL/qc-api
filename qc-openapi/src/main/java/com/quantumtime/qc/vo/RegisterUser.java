package com.quantumtime.qc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description:注册用户dto Created on 2019/09/09 15:04
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@ApiModel(description = "注册所需信息")
public class RegisterUser implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 微信号 */
  @ApiModelProperty("微信号")
  private String wxNumber;

  /** 微信昵称 */
  @ApiModelProperty("微信昵称")
  private String wxNickname;

  /** 用户名 */
  @ApiModelProperty("用户名")
  private String username;

  /** 用户手机号 */
  @ApiModelProperty("用户手机号，可不传")
  private String phone;

  /** 短信验证码 */
  @ApiModelProperty("短信验证码")
  private String verification;

  /** 微信唯一标识 */
  @ApiModelProperty("微信唯一标识")
  private String unionId;

  /** 设备匿名id */
  @ApiModelProperty("anonymousId")
  private String anonymousId;

  /** 地址数据 */
  @ApiModelProperty("地址数据，可不传")
  private AddressParam addressParam;

  /** 性别*/
  @ApiModelProperty("性别1男，2女，3未知")
  private Integer gender;

  /** 头像*/
  @ApiModelProperty("头像：微信头像")
  private String avatar;
}
