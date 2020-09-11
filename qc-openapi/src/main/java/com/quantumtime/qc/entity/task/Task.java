package com.quantumtime.qc.entity.task;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "task")
@Data
@Accessors(chain = true)
@Proxy(lazy=false)
public class Task {

    /**
     * 优质内容
     */
    public static final int TASK_TYPE_GOOD_CONTENT = 0;
    /**
     * 邀请
     */
    public static final int TASK_TYPE_INVITE = 1;
    /**
     * 点赞
     */
    public static final int TASK_TYPE_LIKE = 2;
    /**
     * 转发好友
     */
    public static final int TASK_TYPE_FORWARD = 3;
    /**
     * 关注
     */
    public static final int TASK_TYPE_FOCUS = 4;
    /**
     * 观看视频时长
     */
    public static final int TASK_TYPE_WATCH_TIME = 5;
    /**
     * 门店信息
     */
    public static final int TASK_TYPE_SHOP_INFO = 6;
    /**
     * 添加手机号
     */
    public static final int TASK_TYPE_ADD_PHONE = 7;

    /**
     * 每日任务
     */
    public static final int TASK_DURATION_TYPE_DAY = 0;
    /**
     *  一次性任务
     */
    public static final int TASK_DURATION_TYPE_ONCE = 1;
    /**
     * 长期任务
     */
    public static final int TASK_DURATION_TYPE_LONG_RUN = 2;
    /**
     * 范围类型省
     */
    public static final int TASK_AREA_TYPE_PROVINCE = 0;
    /**
     * 范围类型市
     */
    public static final int TASK_AREA_TYPE_CITY = 1;
    /**
     * 范围类型全国
     */
    public static final int TASK_AREA_TYPE_COUNTRY = 2;


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "fetch")
    private Boolean fetch;

    @Column(name = "type")
    private Integer type;

    @Column(name = "duration_type")
    private Integer durationType;

    @Column(name = "area_type")
    private Integer areaType;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date    endTime;

    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "memo")
    private String memo;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "update_user")
    private String updateUser;
}
