package com.quantumtime.qc.entity.feeds;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "video")
@Data
@Accessors(chain = true)
@Proxy(lazy=false)
public class Video {

    public static final int STATUS_NOT_UPLOAD = 0;
    public static final int STATUS_UPLOADING = 1;
    public static final int STATUS_UPLOADED = 2;
    public static final int STATUS_UPLOAD_FAIL =  3;
    public static final int STATUS_CUT_COMPLETE = 4;
    public static final int STATUS_CUT_FAIL = 5;
    public static final int STATUS_AUDIT_COMPLETE = 6;
    public static final int STATUS_AUDIT_FAIL = 7;
    public static final int STATUS_AUDIT_REVIEW = 11;
    /**
     * 强制下架
     */
    public static final int STATUS_DISABLE = 8;
    public static final int STATUS_DELETED = 9;
    public static final int STATUS_GIVEN_UP = 10;

    public static final int SCOPE_ALL = 0;
    public static final int SCOPE_POI = 1;

    public static final int SOURCE_USER = 0;
    public static final int SOURCE_MIS = 1;


    @Id
    @Column(name = "video_id")
    private String videoId;

    @Column(name = "uid")
    private String uid;

    @Column(name = "source")
    private int source;

    @Column(name = "title")
    private String title;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private String fileSize;

    @Column(name = "description")
    private String description;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "cover_image_id")
    private String coverImageId;

    @Column(name = "cate_id")
    private String cateId;

    @Column(name = "tags")
    private String tags;

    @Column(name = "template_group_id")
    private String templateGroupId;

    @Column(name = "user_data")
    private String userData;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "work_flow_id")
    private String workFlowId;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "upload_address")
    private String uploadAddress;

    @Column(name = "upload_auth")
    private String uploadAuth;

    @Column(name = "address_id")
    private Long addressId;

    /** 经度 */
    @Column(name = "longitude")
    private String longitude;

    /** 纬度 */
    @Column(name = "latitude")
    private String latitude;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = " mis_create_time")
    private Date misCreateTime;

    @Column(name = "status")
    private Integer status;

    @Column(name = "scope")
    private Integer scope;

    @Column(name = "score")
    private Integer score;

    @Column(name = "scope_address_id")
    private Long scopeAddressId;

    @Column(name = "status_machine")
    private Integer statusMachine;

    @Column(name = "status_person")
    private Integer statusPerson;

    @Column(name = "auto_check_time")
    private LocalDateTime autoCheckTime;

    @Column(name = "width")
    private Long width;

    @Column(name = "height")
    private Long height;

    @Column(name = "heat")
    private Integer heat;

    @Column(name = "activity_id")
    private Long activityId;

    @Column(name = "activity_title")
    private String activityTitle;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "shares")
    private Integer shares;

    @Column(name = "related")
    private Integer related;

}
