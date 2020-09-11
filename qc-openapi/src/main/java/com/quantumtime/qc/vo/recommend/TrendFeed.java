package com.quantumtime.qc.vo.recommend;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TrendFeed
 *
 * @author Tablo
 * @blame RD Team
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TrendFeed extends FeedSummary {

    private Long trendId;
    private String summary;
    private String pics;
}
