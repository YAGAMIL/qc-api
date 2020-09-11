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
import java.util.Date;

/**
 * Description:活动实体
 *
 * <p>Created on 2019/12/13 15:15
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiModel(value = "com-quantumtime-qc-entity-activity-Activity")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "activity")
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 活动id，主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "活动id，主键")
    private Long id;
    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String name;

    /**
     * 话题名称
     */
    @ApiModelProperty(value = "话题名称")
    private String conversation;

    /**
     * 活动描述
     */
    @ApiModelProperty(value = "活动描述")
    private String description;

    /**
     * 如果是address_type是0则配置address_id如果address_type为1或者2则配置address_id
     */
    @ApiModelProperty(value = "地址类型")
    private Integer addressType;

    /**
     * 所属地域
     */
    @ApiModelProperty(value = "所属地域")
    private Long addressId;

    /**
     * 所属地域
     */
    @ApiModelProperty(value = "所属地域")
    private String addressName;

    /**
     * 状态，0进行中，1已截止，2无效
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 0代表运营后台活动，1代表商家活动
     */
    @ApiModelProperty(value = "类型")
    private Integer type;
    /**
     * 活动的封面图
     */
    @ApiModelProperty(value = "封面图")
    private String coverUrl;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUser;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "开奖时间")
    private Date lotteryTime;


}
