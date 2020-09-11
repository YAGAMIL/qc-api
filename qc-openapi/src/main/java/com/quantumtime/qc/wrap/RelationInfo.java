package com.quantumtime.qc.wrap;

import com.google.common.base.Converter;
import com.quantumtime.qc.entity.User;
import static com.quantumtime.qc.service.StarFanService.NO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * .Description:用户关系展示 Program:qc-api.Created on 2019-11-19 17:38
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class RelationInfo {
    @ApiModelProperty("用户ID")
    private String uid;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("地址Id")
    private Long addressId;

    @ApiModelProperty("关系")
    private int relationCode = NO;

    @SuppressWarnings("unused")
    public static RelationInfo convert2User(User user) {
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
    public User convert2Info() {
        return new InfoConvert().convert(this);
    }
    /** The type Vo convert. */
    public static class InfoConvert extends Converter<RelationInfo, User> {

        @Override
        protected User doForward(@Nonnull RelationInfo relationInfo) {
            User user = new User();
            BeanUtils.copyProperties(relationInfo, user);
            return user;
        }

        @Override
        protected RelationInfo doBackward(@Nonnull User user) {
            RelationInfo relationInfo = new RelationInfo().setRelationCode(NO);
            BeanUtils.copyProperties(user, relationInfo);
            return relationInfo;
        }
    }
}
