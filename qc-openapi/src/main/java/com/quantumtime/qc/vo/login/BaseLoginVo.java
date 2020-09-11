package com.quantumtime.qc.vo.login;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * Description:基本登录Vo类
 * Program:saas-server
 * </p>
 * Created on 2019-09-03 15:53
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
class BaseLoginVo implements Serializable {


    private static final long serialVersionUID = 1L;
    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 验证码
     */
    private String verification;
}
