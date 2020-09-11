package com.quantumtime.qc.entity.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
 * * Description: 用户举报记录 Created on 2019/12/10 11:13
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Entity
@Table(name = "report_user")
@Accessors(chain = true)
@ApiModel(value = "com-quantumtime-qc-entity-ReportUser")
@Data
public class ReportUser implements Serializable {
    /**
     * 未处理
     */
    public static final int STATUS_NOT_PROCESSED = 0;
    /**
     * 已处理封禁
     */
    public static final int STATUS_PROCESSED_FORBIDDEN = 1;
    /**
     * 已处理未封禁
     */
    public static final int STATUS_PROCESSED_OK = 2;

    private static final long serialVersionUID = 1L;
    /** id */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Long id;
    /** 被举报用户的uid */
    @ApiModelProperty(value = "被举报用户的uid")
    private String uid;
    /** 举报用户uid */
    @ApiModelProperty(value = "举报用户uid")
    private String reportUid;
    /** 举报内容 */
    @ApiModelProperty(value = "举报内容")
    private String reason;
    /** 举报时间 */
    @ApiModelProperty(value = "举报时间")
    private Date createTime;
    /** 处理时间 */
    @ApiModelProperty(value = "处理时间")
    private Date updateTime;
    /** 处理状态 0:未处理 1:已处理封禁 2:已处理未封禁 */
    @ApiModelProperty(value = "处理状态 0:未处理 1:已处理封禁 2:已处理未封禁")
    private Integer status;
    /** 处理意见 */
    @ApiModelProperty(value = "处理意见")
    private String comment;
    /** 审核人员uid */
    @ApiModelProperty(value = "审核人员uid")
    private String auditUid;
}
