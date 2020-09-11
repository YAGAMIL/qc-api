package com.quantumtime.qc.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Description:实体基类 Created on 2019/09/16 17:01
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity implements Serializable {
  @Column(name = "t_create_time")
  private  Date createTime ;
  @Column(name = "t_up_time")
  private Date upTime;
}
