package com.quantumtime.qc.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Converter;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * .Description:用户返回Vo & Created on 2019/10/21 14:06
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "用户信息返回")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户uid */
    private String uid;

    /** 头像url */
    private String avatar;

    /** 昵称 */
    private String nickname;

    /** 性别 */
    @ApiModelProperty("性别1男，2女，0未知")
    private Integer gender;

    /** 用户外显ID */
    private Integer id;

    /** 个性签名 */
    private String signature;

    /** 作品数 */
    private Integer opusSum;

    /** 生日 */
    private Date birthday;

    /** 密码 */
    private String password;

    /** 状态 */
    @JsonIgnore private Integer state;

    /** 地址Id */
    @JsonIgnore private Long addressId;

    /** 手机号 */
    private String phone;

    /** 账户 */
    private String account;

    /** 所属地址信息 */
    private Address address;

    /** The Relation code. 当前登录用户和要看的目的用户之间的关系 */
    private int relationCode;

    /** 粉丝数量 */
    private Integer starSum;

    /** 关注数量 */
    private Integer fanSum;

    /** 点赞作品数 */
    private Integer ClickSum;

    /**
     * Convert 2 personal personal vo.
     *
     * @param user 用户实体
     * @return com.quantumtime.qc.vo.PersonalVo personal vo
     * @date Created on 15:28 2019/10/21 Author: Tablo.
     *     <p>Description:[用户实体转换为Vo]
     */
    @SuppressWarnings("unused")
    public UserInfo convert2Personal(User user) {
        return new VoConvert().convert(user);
    }

    /**
     * Convert 2 user user.
     *
     * @return com.quantumtime.qc.entity.User user
     * @date Created on 15:28 2019/10/21 Author: Tablo.
     *     <p>Description:[Vo转用户实体]
     */
    @SuppressWarnings("unused")
    public User convert2User() {
        return new VoConvert().reverse().convert(this);
    }

    /** The type Vo convert. */
    public static class VoConvert extends Converter<User, UserInfo> {

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
