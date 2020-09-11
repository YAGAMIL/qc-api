package com.quantumtime.qc.entity.information;

import com.quantumtime.qc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description: 信息流实体 Created on 2019/09/18 13:33
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_information_flow")
@Data
public class InformationFlow extends BaseEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "content")
  private String content;

  @Column(name = "type")
  private Byte type;

  @Column(name = "jump_url")
  private String jumpUrl;

  @Column(name = "resource_urls")
  private String resourceUrls;

  @Column(name = "ext_id")
  private Long extId;

  @Column(name = "is_like")
  private Boolean isLike;

  @Column(name = "is_comment")
  private Boolean isComment;

  @Column(name = "is_forward")
  private Boolean isForward;

  @Column(name = "forward_hierarchy")
  private String forwardHierarchy;

  @Column(name = "theme_id")
  private Long themeId;

  @Column(name = "is_delete")
  private Boolean isDelete;

  @Column(name = "create_id")
  private String createId;

  @Column(name = "poi_id")
  private String poiId;

  @Column(name = "like_num")
  private Long likeNum;

  @Column(name = "comment_num")
  private Long commentNum;

  @Column(name = "forward_num")
  private Long forwardNum;

  @Column(name = "forward_result")
  private String forwardResult;

  @Column(name = "scope")
  private Integer scope;

  @Column(name = "address_id")
  private Long addressId;
}
