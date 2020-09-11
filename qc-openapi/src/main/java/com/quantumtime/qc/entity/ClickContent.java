package com.quantumtime.qc.entity;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Description:点击信息记录 Program:qc-api Created on 2019-09-21 16:06
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

@Entity
@Table(name = "click_content")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ClickContent implements Serializable {

  private static final long serialVersionUID = 1L;
  /** 主键（无意义） */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "主键（无意义）")
  private Long id;

  /** 被通知人 */
  @Column(name = "to_uid")
  @ApiModelProperty(value = "被通知人，后续消息推送，暂做冗余")
  private String toUid;

  /** 点击类型id */
  @Column(name = "click_type_id")
  @ApiModelProperty(value = "点击类型id")
  private Integer clickTypeId;

  /** 内容id */
  @Column(name = "content_id")
  @ApiModelProperty(value = "内容id")
  private String contentId;

  /** 内容类型,1动态，2文章 */
  @Column(name = "content_type")
  @ApiModelProperty(value = "内容类型,1动态，0文章")
  private Integer contentType;

  /** 创建人 */
  @ApiModelProperty("创建人uid")
  private String createUid;

  /** 创建时间 */
  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;
}
