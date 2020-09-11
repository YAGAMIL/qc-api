package com.quantumtime.qc.entity.undo;

import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
/**
 * Description: 通知实体 Created on 2019/09/18 13:34
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_notification")
@Entity
@Data
public class Notification extends BaseEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "type")
  private Integer type;

  @Column(name = "biz_id")
  private Long bizId;

  @Column(name = "flow_id")
  private Long flowId;

  @Column(name = "notifi_user_id")
  private String notifiUserId;

  @Column(name = "status")
  private Integer status;

  @Column(name = "title")
  private String title;

  @Column(name = "body")
  private String body;
}
