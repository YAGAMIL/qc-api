package com.quantumtime.qc.entity.undo;


import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_sendmanage")
@Data
@EqualsAndHashCode(callSuper = false)
public class SendManage extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identification_code")
    private String identificationCode;

    @Column(name = "is_send")
    private Boolean isSend;

    @Column(name = "send_time")
    private Date sendTime;

    @Column(name = "send_user")
    private String sendUser;
}
