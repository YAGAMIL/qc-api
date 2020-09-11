package com.quantumtime.qc.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Description: 用户信息VO
 * Created on 2019/10/13 18:09 
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ApiModel(description = "用户信息VO")
public class UserVo {

    private String uid;

    private String avatar;

    private String signature;

    private String nickname;

    private String account;

    private String mail;

    private Integer gender;

    private Date birthday;

    private Integer state;

    private Integer dept;

    private Date createTime;

    private Date upTime;

    private String phone;

    private Long addressId;

    private String house;

}
