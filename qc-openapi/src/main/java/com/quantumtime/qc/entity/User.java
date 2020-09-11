package com.quantumtime.qc.entity;

import com.google.common.base.Converter;
import com.quantumtime.qc.common.entity.BaseEntity;
import com.quantumtime.qc.vo.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Description: 用户JPA实体 Created on 2019/10/12 11:25
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Entity
@Table(name = "tb_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@Proxy(lazy = false)
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 未审核 */
    public static final short AUTH_STATE_NOT = 0;
    /** 审核中 */
    public static final short AUTH_STATE_IN = 1;
    /** 已审核 */
    public static final short AUTH_STATE_SUCCESS = 2;

    /** 个人认证 */
    public static final short AUTH_TYPE_PERSON = 0;
    /** 企业认证 */
    public static final short AUTH_TYPE_ORG = 1;

    public static final long DEFAULT_SCORE = 0;

    /**
     * 用户状态:正常
     */
    public static final short USER_STATUS_NORMAL = 0;
    /**
     * 用户状态:禁言
     */
    public static final short USER_STATUS_FORBID = 1;
    /**
     * 用户状态:封号
     */
    public static final short USER_STATUS_BLOCK = 2;

    @Id
    @GeneratedValue(generator = "jpa-uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "t_uid")
    private String uid;

    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "t_avatar")
    private String avatar;

    @Column(name = "signature")
    private String signature;

    @Column(name = "t_account")
    private String account;

    @Column(name = "t_mail")
    private String mail;

    @Column(name = "t_open_id")
    private String openId;

    @Column(name = "t_nickname")
    private String nickname;

    @Column(name = "t_password")
    private String password;

    @Column(name = "t_gender")
    @ApiModelProperty("性别1男，2女，0未知")
    private Integer gender;

    @Column(name = "t_birthday")
    private Date birthday;

    @Column(name = "t_state")
    private Integer state;

    @Column(name = "t_dept")
    private Integer dept;

    @Column(name = "t_phone")
    private String phone;

    @Column(name = "t_address_id")
    private Long addressId;

    @Column(name = "t_house")
    private String house;

    @Column(name = "t_union_id")
    private String unionId;

    @Column(name = "t_wx_number")
    private String wxNumber;

    @Column(name = "t_wx_nickname")
    private String wxNickname;

    @Column(name = "t_auth_state")
    private short authState;

    @Column(name = "t_project_url")
    private String projectUrl;

    @Column(name = "t_auth_type")
    private short authType;

    @Column(name = "t_memo")
    private String memo;

    @Column(name = "t_hobbies")
    private String hobbies;

    @Column(name = "t_qualify_url_1")
    private String qualifyUrl1;

    @Column(name = "t_qualify_url_2")
    private String qualifyUrl2;

    @Column(name = "t_qualify_url_3")
    private String qualifyUrl3;

    @Column(name = "t_character_name")
    private String characterName;

    @Column(name = "t_character_url")
    private String characterUrl;

    @Column(name = "t_qualify_time")
    private Date qualifyTime;

    @Column(name = "t_star_sum")
    private Integer starSum;

    @Column(name = "t_fan_sum")
    private Integer fanSum;

    @Column(name = "t_is_vest")
    private Boolean isVest;

    @Column(name = "t_score")
    private Long score;

    @Column(name = "t_score_freeze")
    private Long scoreFreeze;

    @Column(name = "t_score_total")
    private Long scoreTotal;

    @Column(name = "status")
    private Short status;

    @Column(name = "forbid_start_time")
    private Date forbidStartTime;

    @Column(name = "forbid_end_time")
    private Date forbidEndTime;

    /** 作品数 */
    @Transient private Integer opusSum;
    /**
     * Convert 2 personal personal vo.
     *
     * @param user 用户实体
     * @return com.quantumtime.qc.vo.PersonalVo personal vo
     * @date Created on 15:28 2019/10/21 Author: Tablo.
     *     <p>Description:[用户实体转换为Vo]
     */
    @SuppressWarnings("unused")
    public User convert2User(UserInfo user) {
        return new InfoConvert().reverse().convert(user);
    }

    /**
     * Convert 2 user user.
     *
     * @return com.quantumtime.qc.entity.User user
     * @date Created on 15:28 2019/10/21 Author: Tablo.
     *     <p>Description:[Vo转用户实体]
     */
    @SuppressWarnings("unused")
    public UserInfo convert2Info() {
        return new InfoConvert().convert(this);
    }

    /** The type Vo convert. */
    public static class InfoConvert extends Converter<User, UserInfo> {

        @Override
        protected UserInfo doForward(@Nonnull User user) {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user, userInfo);
            return userInfo;
        }

        @Override
        protected User doBackward(@Nonnull UserInfo userInfo) {
            User user = new User();
            BeanUtils.copyProperties(userInfo, user);
            return user;
        }
    }
}
