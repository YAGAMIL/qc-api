package com.quantumtime.qc.wrap;

import com.quantumtime.qc.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * .Description:Token中用户解析 & Created on 2019/10/23 12:11
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseInfo {

    private User user;
}
