package com.quantumtime.qc.entity.task;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "good_content_score")
@Data
@Accessors(chain = true)
@Proxy(lazy=false)
public class GoodContentScore {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_user_fetch_id")
    private Long taskUserFetchId;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "score")
    private Long score;
}
