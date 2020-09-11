package com.quantumtime.qc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
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
 * Description:邀请表 Created on 2019/12/03 16:35
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Accessors(chain = true)
@Data
@ApiModel(description = "邀请信息")
@Entity
@Table(name = "invite")
public class Invite implements Serializable {

    public static final int TASK_STATUS_INIT = 0;
    public static final int TASK_STATUS_SUCCESS = 1;
    public static final int TASK_STATUS_FAIL = 2;
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "inviter_id")
    private Integer inviterId;

    @Column(name = "invitee_id")
    private Integer inviteeId;

    @Column(name = "task_status")
    private Integer taskStatus;

    @Column(name = "score")
    private Long score;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
