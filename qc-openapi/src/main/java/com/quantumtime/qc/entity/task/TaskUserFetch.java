package com.quantumtime.qc.entity.task;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task_user_fetch")
@Data
@Accessors(chain = true)
@Proxy(lazy=false)
public class TaskUserFetch {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "expire_time")
    private Date expireTime;

    @Column(name = "count")
    private Integer count;

    @Column(name = "status")
    private Boolean completed;

    @Column(name = "activity_id")
    private Long activityId;

}
