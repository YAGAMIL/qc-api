package com.quantumtime.qc.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@ToString(callSuper = true)
@Entity
@Table(name = "version")
@Data
public class Version {
    /** Column: version_id Remark: 主键 */
    @Id
    @Column(name = "version_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long versionId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "down_url")
    private String downUrl;

    @Column(name = "version_code")
    private Long versionCode;

    @Column(name = "version_name")
    private String versionName;

    @Column(name = "apk_size")
    private String apkSize;

    @Column(name = "md5_key")
    private String md5Key;

    @Column(name = "is_force_update")
    private Integer isForceUpdate;

    @Column(name = "create_time")
    private Date createTime;






}
