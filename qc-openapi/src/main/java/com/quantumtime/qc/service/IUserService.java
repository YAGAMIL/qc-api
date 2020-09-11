package com.quantumtime.qc.service;

import com.quantumtime.qc.common.exception.BizException;
import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.vo.*;
import com.quantumtime.qc.vo.login.LoginUser;
import com.quantumtime.qc.wrap.FansOrStarListWarp;
import java.util.List;
import java.util.Set;

/**
 * .Description:用户业务接口 & Created on 2019/10/21 17:14
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface IUserService extends IBaseService<User, String> {

    /**
     * Find by account user.
     *
     * @param account the account
     * @return the user
     */
    User findByAccount(String account);

    /**
     * Find by open id user.
     *
     * @param openId the open id
     * @return the user
     */
    User findByOpenId(String openId);

    /**
     * Author: Tablo
     *
     * <p>Description:[根据unionId查询用户] Created on 17:15 2019/09/02
     *
     * @param unionId 微信全平台唯一标识
     * @return com.quantumtime.qc.vo.login.LoginResult user info
     */
    UserInfo findByUnionId(String unionId);

    /**
     * Find union id User实体对象.
     *
     * @param unionId the union id
     * @return the user
     */
    User findUnionId2User(String unionId);

    /**
     * Created on 10:22 2019/11/16 Author: Tablo.
     *
     * <p>Description:[判断用户微信是否绑定]
     *
     * @param unionId 微信唯一标识
     * @return java.lang.Boolean boolean
     */
    @SuppressWarnings("unused")
    Boolean isWeChatBind(String unionId);

    /**
     * 获取用户详细信息
     *
     * @param uid the uid
     * @return 操作结果 user vo
     */
    UserVo findUserInfo(String uid);

    /**
     * Find sys user list.
     *
     * @return 马甲账号 list
     */
    List<SysUser> findSysUser();

    /**
     * Count by dept int.
     *
     * @param deptId the dept id
     * @return the int
     */
    @SuppressWarnings("unused")
    int countByDept(Integer deptId);

    /**
     * Find by phone user.
     *
     * @param phone the phone
     * @return the user
     */
    User findByPhone(String phone);

    /**
     * Created on 17:13 2019/10/21 Author: Tablo.
     *
     * <p>Description:[根据手机号查询用户信息]
     *
     * @param phone 手机号
     * @return com.quantumtime.qc.vo.UserInfo user info
     */
    @SuppressWarnings("unused")
    UserInfo findInfoByPhone(String phone);

    /**
     * 真实性认证第一步
     *
     * @param vo the vo
     * @return boolean boolean
     */
    Boolean unverifiedFirst(UnverifiedVo vo);

    /**
     * 真实性认证第二步
     *
     * @return boolean boolean
     */
    Boolean unverifiedSecond();

    /**
     * Rollback to unverified first boolean.
     *
     * @return the boolean
     */
    Boolean rollbackToUnverifiedFirst();

    /**
     * 真实性认证第三步
     *
     * @param identificationCode the identification code
     * @return boolean boolean
     */
    Boolean unverifiedThird(String identificationCode);

    /**
     * Author: Tablo
     *
     * <p>Description:[注册最终流程] Created on 17:51 2019/10/08
     *
     * @param registerUser 注册信息
     * @return com.quantumtime.qc.vo.LoginResult user info
     */
    UserInfo registerEnd(RegisterUser registerUser);

    /**
     * 修改地址
     *
     * @param vo the vo
     * @return boolean boolean
     */
    Boolean modifyAddress(UnverifiedVo vo);

    /**
     * 修改用户信息
     *
     * @param vo 修改后的信息
     * @return 是否成功 boolean
     */
    Boolean modifyUser(UserVo vo);

    /**
     * 根据昵称模糊匹配同一小区用户
     *
     * @param nickName 昵称
     * @return 用户List list
     */
    List<User> findListLikeNickName(String nickName);

    //    LoginUser login(LoginUser user);

    /**
     * Modify phone second boolean.
     *
     * @param user the user
     * @return the boolean
     */
    Boolean modifyPhoneSecond(LoginUser user);

    /**
     * Author: Tablo
     *
     * <p>Description:[更新用户信息] Created on 16:45 2019/09/03
     *
     * @param newUser 用户新信息
     */
    void updateUser(User newUser);

    /**
     * Qualify.
     *
     * @param qualifyVo the qualify vo
     * @throws BizException the biz exception
     */
    void qualify(QualifyVo qualifyVo) throws BizException;

    /**
     * Qualify callback.
     *
     * @param qualifyVo the qualify vo
     */
    void qualifyCallback(QualifyVo qualifyVo);

    /**
     * Find all by ids list.
     *
     * @param ids the ids
     * @return the list
     */
    List<User> findAllByIds(Set<String> ids);

    /**
     * Author: Tablo
     *
     * <p>Description:[获取数据库中最大的id] Created on 16:09 2019/10/12
     *
     * @return java.lang.Integer max id
     */
    Integer getMaxId();

    /**
     * Author: Tablo
     *
     * <p>Description:[根据Uid查询ID] Created on 16:37 2019/10/13
     *
     * @param uid 用户主键
     * @return java.lang.Integer current id
     */
    @SuppressWarnings("unused")
    Integer getCurrentId(String uid);

    /**
     * Find all by ids list.
     *
     * @param ids the ids
     * @return the list
     */
    List<User> findAllByIds(List<String> ids);

    /**
     * Personal detail user info.
     *
     * @param uid the uid
     * @return the user info
     */
    UserInfo personalDetail(String uid);

    /**
     * Change birthday boolean.
     *
     * @param birthday 生日
     * @return java.lang.Boolean boolean
     * @date Created on 16:10 2019/10/16 Author: wmw.
     *     <p>Description:[]
     */
    Boolean changeBirthday(String birthday);

    /**
     * Created on 19:42 2019/11/08 Author: Tablo.
     *
     * <p>Description:[设置/更新生活圈]
     *
     * @param addressParam 地址信息
     * @return java.lang.Boolean boolean
     */
    Boolean updateAddress(AddressParam addressParam);

    /**
     * Author: wmw Description:[修改用户性别] Created on 2019/10/15
     *
     * @param gender 性别
     * @return Boolean boolean
     */
    Boolean updateGender(Integer gender);

    /**
     * Created on 20:28 2019/11/08 Author: Tablo.
     *
     * <p>Description:[根据用户信息获取用户历史生活圈]
     *
     * @param currentUid the current uid
     * @param currentAddressId the current address id
     * @return java.util.List<com.quantumtime.qc.entity.poi.Address> list
     */
    List<Address> queryRecords(String currentUid, Long currentAddressId);

    /**
     * Created on 14:28 2019/11/13 Author: Tablo.
     *
     * <p>Description:[删除用户某个地址的历史记录]
     *
     * @param uid 用户ID
     * @param addressId 生活圈Id
     * @return boolean boolean
     */
    boolean removeRecords(String uid, Long addressId);
    /**
     * Created on 15:35 2019/11/14 Author: dong.
     *
     * <p>Description:[用户的粉丝列表]
     *
     * @param fansOrStarListWarp the fans or star list warp
     * @return List<FansOrStarListWarp> list
     */
    List<FansOrStarListVo> fansList(FansOrStarListWarp fansOrStarListWarp);

    /**
     * Created on 15:35 2019/11/14 Author: dong.
     *
     * <p>Description:[用户的关注列表]
     *
     * @param fansOrStarListWarp the fans or star list warp
     * @return List<FansOrStarListWarp> list
     */
    List<FansOrStarListVo> starList(FansOrStarListWarp fansOrStarListWarp);

    void reportUser(ReportUserRequest reportUserRequest) throws Exception;

    void addUserBlacklist(String fromUid, String toUid) throws Exception;

    void removeUserBlacklist(String fromUid, String toUid);

    List<User> getAllUserBlacklist(String uid);

    boolean checkInBlacklist(String fromUid, String toUid);
}
