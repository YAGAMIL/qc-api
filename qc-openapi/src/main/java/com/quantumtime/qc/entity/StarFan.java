package com.quantumtime.qc.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * .Description: 关注关系表单 & Created on 2019/11/12 16:13
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiModel(value = "com-quantumtime-qc-entity-StarFan")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "star_fan")
public class StarFan implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 是否已关注，用于实现类中获取是否关注的方法返回判断 */
    @Transient boolean isAttention;
    /** id */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Long id;
    /** 被关注者id */
    @ApiModelProperty(value = "被关注者id")
    private String starUid;
    /** 粉丝id */
    @ApiModelProperty(value = "粉丝id")
    private String fanUid;
    /** 是否双向关注 0:否 1:是 */
    @ApiModelProperty(value = "是否双向关注 0:否 1:是")
    private Boolean twoWay;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /** 关注排序 */
    @ApiModelProperty(value = "关注排序")
    private Integer starRank;
    /** 粉丝排序 */
    @ApiModelProperty(value = "粉丝排序")
    private Integer fanRank;
}


