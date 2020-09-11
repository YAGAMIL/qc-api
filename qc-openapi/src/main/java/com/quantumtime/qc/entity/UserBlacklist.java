package com.quantumtime.qc.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_blacklist")
@Data
@Accessors(chain = true)
@Proxy(lazy=false)
public class UserBlacklist {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_uid")
    private String fromUid;

    @Column(name = "to_uid")
    private String toUid;

    @Column(name = "create_time")
    private Date createTime;
}
