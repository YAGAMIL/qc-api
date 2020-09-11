package com.quantumtime.qc.entity.score;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description: 积分记录表 Created on 2019/12/03 16:35
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "score_log")
public class ScoreLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "uid")
  private String uid;

  @Column(name = "score")
  private Long score;

  @Column(name = "type")
  private Boolean type;

  @Column(name = "tran_type")
  private Integer tranType;

  @Column(name = "record_id")
  private Long recordId;

  @Column(name = "description")
  private String description;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @Column(name = "create_time")
  private LocalDateTime createTime;
}
