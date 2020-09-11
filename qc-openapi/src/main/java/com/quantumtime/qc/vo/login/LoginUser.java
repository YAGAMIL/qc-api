package com.quantumtime.qc.vo.login;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * .Description:旧版登陆VO & Created on 2019/10/16 17:52
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class LoginUser implements Serializable {

    private static final long serialVersionUID = -6225159294199791083L;
    private String phoneNumber;

    private String password;

    private String verification;

    private String token;

    private String oldVerification;

}
