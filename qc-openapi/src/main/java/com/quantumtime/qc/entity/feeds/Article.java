package com.quantumtime.qc.entity.feeds;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * .Description:文章实体类 & Created on 2019/10/16 17:45
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ToString(callSuper = true)
@Entity
@Table(name = "article")
@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Column: article_id Remark: 主键
     */
    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    /**
     * Column: title Remark: 文章标题
     */
    @Column(name = "title")
    private String title;

    /**
     * Column: author_uid Remark: 作者
     */
    @Column(name = "author_uid")
    private String authorUid;

    /**
     * Column: cover_img Remark: 封面图片
     */
    @Column(name = "cover_img")
    private String coverImg;

    /**
     * Column: url_source Remark: 来源(冗余)
     */
    @Column(name = "url_source")
    private String urlSource;

    /**
     * Column: address_id Remark: 发表地点id
     */
    @Column(name = "address_id")
    private Long addressId;

    /**
     * Column: review_status Remark: 审核状态,0上架,1.下架,-1撤销
     */
    @Column(name = "review_status")
    private Integer reviewStatus;

    /**
     * Column: type Remark: 类型:0图文,1音频,2视频
     */
    @Column(name = "type")
    private Integer type;

    /**
     * Column: scope Remark: 0小区可见/1全国可见。。。
     */
    @Column(name = "scope")
    private Integer scope;

    /**
     * Column: content Remark: 正文内容
     */
    @Column(name = "content")
    private String content;

    /**
     * Column: summary Remark: 文章摘要
     */
    @Column(name = "summary")
    private String summary;

    /**
     * Column: createTime Remark: 文章发布时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
