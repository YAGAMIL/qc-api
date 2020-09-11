package com.quantumtime.qc.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * .Description:粉丝关系列表 & Created on 2019/11/18 10:37
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class FansOrStarListVo {
    private String uid;
    private String nickName;
    private Integer id;
    private String avatar;
    /** 关系标识 */
    private int relationCode;
}
