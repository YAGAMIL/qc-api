package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

/**
 * .Description: & Created on 2019/8/30 15:56
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Created on 15:50 2019/10/30 Author: Tablo.
     *
     * <p>Description:[根据账户信息查询用户]
     *
     * @param account 账户
     * @return com.quantumtime.qc.entity.User user
     */
    User findByAccount(String account);

    /**
     * Author: Tablo
     *
     * <p>Description:[根据OpenId查询用户] Created on 17:20 2019/09/02
     *
     * @param openId 微信单平台唯一标识
     * @return com.quantumtime.qc.entity.User user
     */
    User findByOpenId(String openId);

    /**
     * Author: Tablo
     *
     * <p>Description:[根据unionId查询用户信息] Created on 17:17 2019/09/02
     *
     * @param unionId 微信唯一标识
     * @return com.quantumtime.qc.entity.User user
     */
    User findByUnionId(String unionId);

    /**
     * Created on 15:51 2019/10/30 Author: Tablo.
     *
     * <p>Description:[查询部门总人数]
     *
     * @param deptId 部门Id
     * @return int int
     */
    int countByDept(Integer deptId);

    /**
     * Created on 15:52 2019/10/30 Author: Tablo.
     *
     * <p>Description:[根据邮箱或账户查询总数]
     *
     * @param account 邮箱活账户
     * @return java.lang.Integer integer
     */
    @Query(value = "select count(u) from User as u where u.account = ?1 or u.mail = ?1")
    Integer countByAccountOrMail(String account);

    /**
     * Created on 15:53 2019/10/30 Author: Tablo.
     *
     * <p>Description:[查询手机号注册数目]
     *
     * @param phone 手机号
     * @return int int
     */
    @SuppressWarnings("unused")
    int countByPhone(String phone);

    /**
     * Created on 15:53 2019/10/30 Author: Tablo.
     *
     * <p>Description:[根据手机号查询]
     *
     * @param phone 手机号
     * @return com.quantumtime.qc.entity.User user
     */
    @Query(nativeQuery = true, value = "select u.* from tb_user as u where u.t_phone = ?1")
    User findPhone(String phone);

    /**
     * Created on 15:54 2019/10/30 Author: Tablo.
     *
     * <p>Description:[like昵称查询某区域下的用火狐]
     *
     * @param nickName 昵称
     * @param addressId 地址Id
     * @return java.util.List<com.quantumtime.qc.entity.User> list
     */
    @Query(value = "select u from User as u where u.addressId = ?2 and u.nickname like %?1%")
    List<User> findListLikeNickName(String nickName, Long addressId);

    /**
     * Find sys user list.
     *
     * @return the list
     */
    @Query(
            value =
                    "select u.t_uid as uid, u.t_nickname as userNickName, u.t_phone as userPhone, a.op_phone as opPhone, a.op_name as opName from tb_user as u inner join sys_account as a on u.t_phone=a.user_phone",
            nativeQuery = true)
    List<Object[]> findSysUser();

    /**
     * Author: Tablo
     *
     * <p>Description:[获取数据库中最大的id] Created on 16:09 2019/10/12
     *
     * @return java.lang.Integer integer
     */
    @Query(value = "SELECT MAX(id) FROM tb_user", nativeQuery = true)
    Integer maxId();

    /**
     * Author: Tablo
     *
     * <p>Description:[根据Uid查询ID] Created on 16:37 2019/10/13
     *
     * @param uid 用户主键
     * @return java.lang.Integer integer
     */
    @Query(value = "SELECT id FROM tb_user WHERE t_uid = ?1", nativeQuery = true)
    Integer findIdByUid(String uid);

    /**
     * Author: Tablo
     *
     * <p>Description:[根据昵称查询] Created on 11:34 2019/10/14
     *
     * @param nickName 昵称
     * @return com.quantumtime.qc.entity.User user
     */
    User findUserByNickname(String nickName);

    /**
     * Created on 15:54 2019/10/30 Author: Tablo.
     *
     * <p>Description:[根据UID查询]
     *
     * @param uid uid
     * @return com.quantumtime.qc.entity.User user
     */
    User findByUid(String uid);

    /**
     * Created on 15:54 2019/11/15 Author: dong.
     *
     * <p>Description:[批量查询uid的信息查询]
     *
     * @param uid the uid
     * @return List<User> list
     */
    @Query(value = "SELECT * FROM tb_user WHERE t_uid in (?1)", nativeQuery = true)
    List<User> findByUidList(List<String> uid);

    /**
     * Author: Tablo
     *
     * <p>Description:[查找马甲号] Created on 12:08 2019/11/30
     *
     * @return java.util.List<com.quantumtime.qc.entity.User>
     */
    @Query(value = "SELECT * FROM qc.tb_user WHERE t_is_vest = true", nativeQuery = true)
    List<User> findFaker();

    @Query(value = "SELECT t_uid FROM qc.tb_user WHERE t_is_vest = true", nativeQuery = true)
    List<String> robotUidList();

    @Query(value = "SELECT * FROM qc.tb_user WHERE id in(?1)", nativeQuery = true)
    List<User> findByIdList(List<Integer> id);

    @Query(value = "SELECT * FROM qc.tb_user WHERE t_create_time>?1 and id in (select invitee_id from invite where task_status=0 and create_time>?1)" , nativeQuery = true)
    List<User> findNewInviteUser(Date startDate);

    @Query(value = "SELECT u.* FROM tb_user u, tb_address a WHERE u.t_address_id=a.id and a.city=?1 and u.t_create_time>?2 and u.id in (select invitee_id from invite where task_status=0 and create_time>?2)" , nativeQuery = true)
    List<User> findNewInviteUserCity(String city, Date startDate);

    @Query(value = "SELECT u.* FROM tb_user u, tb_address a WHERE u.t_address_id=a.id and a.province=?1 and u.t_create_time>?2 and u.id in (select invitee_id from invite where task_status=0 and create_time>?2)" , nativeQuery = true)
    List<User> findNewInviteUserProvince(String province, Date startDate);



    User findById(Integer id);

    /**
     * Author: Tablo
     *
     * <p>Description:[加行锁读取用户信息] Created on 15:42 2019/12/04
     *
     * @param uid 用户uid
     * @return com.quantumtime.qc.entity.User
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    User findUserByUid(String uid);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    User findUserById(Integer id);


    @Query(value = "SELECT u.* FROM tb_user u, user_blacklist b WHERE u.t_uid=b.to_uid AND b.from_uid=?1 ORDER BY b.`create_time` DESC", nativeQuery = true)
    List<User> getBlackListUsers(String uid);

    @Query(value = "SELECT COUNT(*) FROM user_blacklist WHERE to_uid = ?2 AND from_uid=?1 OR (from_uid = ?2 AND to_uid = ?1)" , nativeQuery = true)
    Integer getBlackResult(String fromUid, String toUid);
}
