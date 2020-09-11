package com.quantumtime.qc.entity.feeds;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "videos_by_provider")
@Data
@Accessors(chain = true)
public class HaotuVideo {
    @Id
    @Column(name = "video_id")
    private String videoId;

    @Column(name = "uid")
    private String uid;

    @Column(name = "source")
    private int source;

    @Column(name = "title")
    private String title;

    @Column(name = "uname")
    private String uName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "publish_time")
    private Date publishTime;

    @Column(name = "play_num")
    private Long playNum;

    @Column(name = "like_num")
    private Long likeNum;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "tags")
    private String tags;

    @Column(name = "video_w")
    private Integer videoWidth;

    @Column(name = "video_h")
    private Integer videoHeight;

    @Column(name = "category")
    private String category;

    @Column(name = "channel_id")
    private String channelId;
}
