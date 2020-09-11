package com.quantumtime.qc.entity.information;

import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description: 信息流提交记录 Created on 2019/09/18 13:37
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_information_flow_commit")
@Data
public class InformationFlowCommit extends BaseEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "information_flow_id")
  private Long informationFlowId;

  @Column(name = "content")
  private String content;

  @Column(name = "create_id")
  private String createId;

  @Column(name = "is_delete")
  private Boolean isDelete;
}
