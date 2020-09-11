package com.quantumtime.qc.entity.undo;

import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description: 用户主题关系 Created on 2019/09/18 13:36
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rel_user_theme")
@Data
public class RelUserAndTheme extends BaseEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "theme_id")
  private Long themeId;

  @Column(name = "poi_id")
  private String poiId;

  @Column(name = "address_id")
  private Long addressId;
}
