package com.quantumtime.qc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * .Description: & Created on 2019/11/01 16:49
 *
 * @author <a>Gan</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {

    private String uid;

    private String userNickName;

    private String userPhone;

    private String opPhone;

    private String opName;
}
