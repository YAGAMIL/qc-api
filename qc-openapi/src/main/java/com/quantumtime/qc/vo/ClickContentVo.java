package com.quantumtime.qc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * .Description:dong & Created on 2019/10/28 12:02
 *
 * @author <a>董瑞</a>
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ClickContentVo {
    Integer type;
    Object object;
}
