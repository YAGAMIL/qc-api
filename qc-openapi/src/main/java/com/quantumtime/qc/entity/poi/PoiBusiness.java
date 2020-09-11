package com.quantumtime.qc.entity.poi;

import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Description:小区商圈关系 Program:qc-api Created on 2019-09-17 15:58
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "poi_business")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoiBusiness extends BaseEntity {


  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  /** 商圈Id */
  @NonNull
  @Column(name = "business_area_id")
  private Long businessAreaId;

  /** 小区ID，真·主键 */
  @Column(name = "poi_id")
  @NonNull
  private String poiId;
}
