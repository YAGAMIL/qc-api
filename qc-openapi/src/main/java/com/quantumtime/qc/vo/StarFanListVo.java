package com.quantumtime.qc.vo;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * .Description:Dong & Created on 2019/11/26 11:28
 *
 * @author 董瑞
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class StarFanListVo {
    private String thisUid;
    private List<String> uidList;
}
