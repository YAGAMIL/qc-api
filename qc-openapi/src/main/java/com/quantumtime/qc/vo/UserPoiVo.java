package com.quantumtime.qc.vo;

import com.quantumtime.qc.entity.User;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class UserPoiVo {
    private String uid;
    
    private String avatar;

    
    private String account;

    
    private String mail;

    
    private String openId;

    
    private String nickname;

    
    private String password;

    
    private Integer gender;

    
    private Date birthday;

    
    private Integer state;

    
    private Integer dept;

    
    private String phone;

    
    private Long addressId;

    
    private String house;

    
    private String unionId;

    
    private String wxNumber;

    
    private String wxNickname;

    
    private short authState;

    
    private String projectUrl;

    
    private short authType;

    
    private String memo;

    
    private String hobbies;

    
    private String qualifyUrl1;

    
    private String qualifyUrl2;

    
    private String qualifyUrl3;

    
    private String characterName;

    
    private String characterUrl;

    
    private Date qualifyTime;

    private String poiName;

    public UserPoiVo(User user) {
        this.account = user.getAccount();
        this.addressId = user.getAddressId();
        this.authState = user.getAuthState();
        this.authType = user.getAuthType();
        this.avatar = user.getAvatar();
        this.birthday = user.getBirthday();
        this.characterName = user.getCharacterName();
        this.characterUrl = user.getCharacterUrl();
        this.dept = user.getDept();
        this.gender = user.getGender();
        this.hobbies = user.getHobbies();
        this.house = user.getHouse();
        this.mail = user.getMail();
        this.memo = user.getMemo();
        this.nickname = user.getNickname();
        this.openId = user.getOpenId();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.projectUrl = user.getProjectUrl();
        this.qualifyTime = user.getQualifyTime();
        this.qualifyUrl1 = user.getQualifyUrl1();
        this.qualifyUrl2 = user.getQualifyUrl2();
        this.qualifyUrl3 = user.getQualifyUrl3();
        this.state = user.getState();
        this.uid = user.getUid();
        this.unionId = user.getUnionId();
        this.wxNickname = user.getWxNickname();
        this.wxNumber = user.getWxNumber();
    }
}
