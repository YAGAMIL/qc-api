package com.quantumtime.qc.entity.poi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Description:商圈实体 Program:qc-api Created on 2019-09-17 15:49
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Table(name = "business_area")
@ToString(callSuper = true)
@Accessors(chain = true)
@Entity
@Data
public class Business extends BaseEntity {

  private static final long serialVersionUID = 1L;

  /** 商圈Id */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "business_area_id")
  private Long businessAreaId;

  /** 商圈名称 */
  @Column(name = "business_area")
  private String businessArea;

  /** 区编码，"110101" */
  @Column(name = "ad_code")
  private String adCode;

  /** 经度 */
  @Column(name = "longitude")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private String longitude;

  /** 纬度 */
  @Column(name = "latitude")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private String latitude;
}
