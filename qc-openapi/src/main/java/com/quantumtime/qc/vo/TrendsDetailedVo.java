package com.quantumtime.qc.vo;

import com.quantumtime.qc.entity.feeds.Feeds;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description:
 * Created on 2019/11/29 11:37
 *
 * @author Unkonwn
 * @version 1.0
 * @blame RD Team
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class TrendsDetailedVo extends Feeds{
    private Integer like;
    private Integer view;
    private Boolean userLike;
}
