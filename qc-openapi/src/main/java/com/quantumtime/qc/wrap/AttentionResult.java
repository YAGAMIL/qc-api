package com.quantumtime.qc.wrap;

import com.quantumtime.qc.entity.StarFan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * .Description: 是否关注及其对象 Program:qc-api.Created on 2019-11-12 18:28
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AttentionResult {
    /** 关系实体 无任何关系 */
    StarFan starFan;

    /** 是否关注 当前登录用户已经关注该目标用户 */
    boolean isAttention;

    /** The Is fan. 该目标用户已经关注了当前登录用户，是否正向关系，非双向关注且FanUid为粉丝 */
    boolean positive;

    /** The Is null.无任何关系 */
    boolean isNull;

    /** The Relation code. 关系数字 */
    int relationCode;
}
