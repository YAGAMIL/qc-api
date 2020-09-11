package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.StarFan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * .Description:粉丝关系持久层 Program:qc-api.Created on 2019-11-12 16:29
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Repository
public interface StarFanRepository extends JpaRepository<StarFan, Long>, JpaSpecificationExecutor<StarFan> {
    /**
     * Created on 17:48 2019/11/12 Author: Tablo.
     *
     * <p>Description:[根据粉丝ID及被关注Id查询关系]
     *
     * @param fanUid 粉丝Id
     * @param starUid 被关注Id
     * @return com.quantumtime.qc.entity.StarFan star fan
     */
    @SuppressWarnings("unused")
    StarFan findByFanUidAndStarUid(String fanUid, String starUid);

    /**
     * Created on 17:51 2019/11/12 Author: Tablo.
     *
     * <p>Description:[查询用户关系]
     *
     * @param id1 用户1
     * @param id2 用户2
     * @return com.quantumtime.qc.entity.StarFan star fan
     */
    @Query(
            value = "select * from star_fan where fan_uid = ?1 and star_uid = ?2 or (fan_uid = ?2 and star_uid = ?1)",
            nativeQuery = true)
    StarFan findAttention(String id1, String id2);

    /**
     * S 1 list.
     *
     * @param uid the uid
     * @return the list
     */
    @Query(value = "select fan_uid from star_fan where star_uid=?1 and two_way=1 ", nativeQuery = true)
    List<String> s1(List<String> uid);

    /**
     * Created on 20:29 2019/11/13 Author: Tablo.
     *
     * <p>Description:[删除记录]
     *
     * @param id1 用户1
     * @param id2 用户2
     * @return int int
     */
    @Query(
            value = "delete from star_fan where fan_uid = ?1 and star_uid = ?2 or (fan_uid = ?2 and star_uid = ?1) ",
            nativeQuery = true)
    int remove(String id1, String id2);

    /**
     * Star fan list list.
     *
     * @param uid the uid
     * @param thisUid the this uid
     * @return the list
     */
    @Query(
            value =
                    "select * from star_fan where fan_uid = ?2 and  star_uid in (?1) or (  fan_uid in (?1) and star_uid = ?2) ",
            nativeQuery = true)
    List<StarFan> starFanList(List<String> uid, String thisUid);

    /**
     * Fans list list.粉丝列表
     *
     * @param uid the uid 用户UID
     * @param pageNumber the page number 分页页码
     * @param pageSize the page size 分页Size
     * @return the list
     */
    @Query(value = "select *  from star_fan where fan_uid=?1 order by create_time desc limit ?2,?3", nativeQuery = true)
    List<StarFan> fansList(String uid, Integer pageNumber, Integer pageSize);
    /**
     * Stars list list.我关注的列表
     *
     * @param uid the uid
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return the list
     */
    @Query(
            value = "select *  from star_fan where star_uid=?1 order by create_time desc limit ?2,?3",
            nativeQuery = true)
    List<StarFan> starsList(String uid, Integer pageNumber, Integer pageSize);

    /**
     * Find star fan by pageable page. 查询某用户关注的人
     *
     * @param fanUid the fan uid
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = "select s from StarFan s  where s.fanUid=?1 or (s.starUid=?1 and s.twoWay=true)")
    Page<StarFan> findStarsByPage(String fanUid, Pageable pageable);

    /**
     * Find fans by page page. 查询某用户的粉丝
     *
     * @param starUid the fan uid
     * @param pageable the pageable
     * @return the page
     */
    @Query(
            value =
                    "select f from StarFan f  where f.starUid=?1 or (f.fanUid=?1 and f.twoWay=true) order by f.createTime desc")
    Page<StarFan> findFansByPage(String starUid, Pageable pageable);

  /**
   * Author: Tablo
   *
   * <p>Description:[查询该用户的所有关系] Created on 15:13 2019/12/12
   *
   * @param uidList 用户uid
   * @return java.util.List<com.quantumtime.qc.entity.StarFan>
   */
  @Query(
      value = "select *  from star_fan where star_uid in ?1 or fan_uid in ?1",
      nativeQuery = true)
  List<StarFan> findUserFanStar(List<String> uidList);
}
