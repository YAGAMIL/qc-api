package com.quantumtime.qc.entity.information;

import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description: 信息流主题 Created on 2019/09/18 13:33
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_information_flow_theme")
@Data
public class InformationFlowTheme extends BaseEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "details")
  private String details;

  @Column(name = "t_create_id")
  private String createId;

  @Column(name = "image")
  private String image;

  @Column(name = "address_id")
  private Long addressId;
}
