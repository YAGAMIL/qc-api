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
 * Description:活动-任务关系 Created on 2019/12/13 15:16
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiModel(value = "com-quantumtime-qc-entity-activity-ActivityTask")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "activity_task")
public class ActivityTask implements Serializable {
  private static final long serialVersionUID = 1L;
  /** 自增主键 */
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "自增主键")
  private Long id;
  /** 活动id */
  @ApiModelProperty(value = "活动id")
  private Long activityId;
  /** 任务id */
  @ApiModelProperty(value = "任务id")
  private Long taskId;
  /** 奖励积分 */
  @ApiModelProperty(value = "奖励积分")
  private Long rewardScore;
  /** 是否主任务，true为主任务，false为辅助任务 */
  @ApiModelProperty(value = "是否主任务，true为主任务，false为辅助任务")
  private Integer isMain;
  /** 创建时间 */
  @ApiModelProperty(value = "创建时间")
  private Date createTime;
}
