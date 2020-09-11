package com.quantumtime.qc.entity.task;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task_rule")
@Data
@Accessors(chain = true)
@Proxy(lazy=false)
public class TaskRule {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "param1_name")
    private String param1Name;

    @Column(name = "param1_value")
    private String param1Value;

    @Column(name = "param2_name")
    private String param2Name;

    @Column(name = "param2_value")
    private String param2Value;

    @Column(name = "param3_name")
    private String param3Name;

    @Column(name = "param3_value")
    private String param3Value;

    @Column(name = "param4_name")
    private String param4Name;

    @Column(name = "param4_value")
    private String param4Value;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
