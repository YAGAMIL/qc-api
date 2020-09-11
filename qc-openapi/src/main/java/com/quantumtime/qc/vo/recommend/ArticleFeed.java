package com.quantumtime.qc.vo.recommend;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: Created on 2019/11/29 11:38
 *
 * @author Unknown
 * @version 1.0
 * @blame RD Team
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleFeed extends FeedSummary {

    private Long articleId;
    private String title;
    private String coverUrl;
}
