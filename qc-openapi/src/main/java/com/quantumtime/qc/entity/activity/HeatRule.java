package com.quantumtime.qc.entity.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;

import static com.quantumtime.qc.common.utils.GlobalUtils.getNullNumElse;
import static com.quantumtime.qc.common.utils.GlobalUtils.getNullSumElse;

/**
 * Description: 热度规则 Created on 2019/12/17 17:59
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiModel(value = "com-quantumtime-qc-entity-activity-HeatRule")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "heat_rule")
public class HeatRule implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 主键 */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 活动id */
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /** 地址id */
    @ApiModelProperty(value = "地址id")
    private Long addressId;

    /** 点赞系数 */
    @ApiModelProperty(value = "点赞系数")
    private Integer like;

    /** 查看系数 */
    @ApiModelProperty(value = "查看系数")
    private Integer view;

    /** 评分系数 */
    @ApiModelProperty(value = "评分系数")
    private Integer score;

    /** 分享系数 */
    @ApiModelProperty(value = "分享系数")
    private Integer share;

    /** 相关系数 */
    @ApiModelProperty(value = "相关系数")
    private Integer related;

    /** 总系数 */
    @ApiModelProperty(value = "总系数")
    private Integer coefficientSum;

    public HeatRule init() {
        return this.setCoefficientSum(getNullSumElse(Arrays.asList(like, view, score, share, related)));
    }

    public HeatRule initNullable() {
        int likeInt = getNullNumElse(this.like);
        int scoreInt = getNullNumElse(this.score);
        int viewInt = getNullNumElse(this.view);
        int relatedInt = getNullNumElse(this.related);
        int shareInt = getNullNumElse(this.share);
        HeatRule result = HeatRule.builder()
                .id(id)
                .activityId(activityId)
                .addressId(addressId)
                .like(likeInt)
                .score(scoreInt)
                .view(viewInt)
                .related(relatedInt)
                .share(shareInt)
                .build();
        return result.setCoefficientSum(likeInt + viewInt + scoreInt + shareInt + relatedInt);
    }
}
