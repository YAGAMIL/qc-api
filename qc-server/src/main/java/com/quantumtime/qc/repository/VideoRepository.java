package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.feeds.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, String> {

  @Modifying
  @Query(value = "update Video v set v.status=9 where v.videoId in ?1")
  void setDelete(List<String> list);

  @Query(value = "select v from Video as v where v.status=6 order by v.createTime desc ")
  List<Video> findPlayable();

  @Query(
      value = "select count(*)from video v where v.uid=?1 and v.status>=5 and v.status!=9 ",
      nativeQuery = true)
  Integer coverSum(String uid);

  @Query(value = "select count(*)from video v where v.uid=?1 and v.status=6", nativeQuery = true)
  Integer coverOtherSum(String uid);

  @Query(
      value =
          "select v.* from video v where v.uid=?1 and v.status>=5 and v.status!=9 order by create_time desc limit ?2,?3 ",
      nativeQuery = true)
  List<Video> findByMyUids(String uid, Integer pageNumber, Integer pageSize);

  @Query(
      value =
          "select v.* from video v where v.uid=?1 and v.status=6 order by create_time desc limit ?2,?3 ",
      nativeQuery = true)
  List<Video> findByOtherUids(String uid, Integer pageNumber, Integer pageSize);

  @Query(
      value = "select v.* from video v where v.video_id in ?1 and v.status>=6  ",
      nativeQuery = true)
  List<Video> findByClickUid(List<String> uid);

  @Query(
      value =
          "select v.* from video v where v.status=6 and v.scope_address_id=?1 and v.create_time>=?2 order by v.create_time desc limit ?3",
      nativeQuery = true)
  List<Video> findByIdLimit(Long addressId, Date startTime, int num);

  @Query(
      value =
          "select v.* from video v where v.status=6 and v.scope_address_id in ?1 and v.scope=1 and v.create_time>=?2 order by v.create_time desc limit ?3",
      nativeQuery = true)
  List<Video> findAllByIdsLimit(List<Long> addressList, Date startTime, int num);

  @Query(
      value =
          "select v from Video as v where v.status=6 and v.scope=0 and v.source=1 and v.createTime>=?1 order by v.createTime desc")
  List<Video> findOpenLimit(Date startTime);

  @Query(
      value =
          "select v.* from video v where v.status=6 and v.scope=0 and v.source=1 and v.create_time>=?1 order by v.create_time desc limit ?2",
      nativeQuery = true)
  List<Video> findOpenLimitNum(Date startTime, int num);

  @Query(
      value =
          "select v.* from video v, tb_address a where v.scope_address_id=a.id and a.city=?1 and v.status=6 and v.scope=1 and v.source=0 and v.create_time>=?2 order by v.create_time desc limit ?3",
      nativeQuery = true)
  List<Video> findCityLimit(String city, Date startDate, int num);

  @Query(
      value =
          "select v.* from video v, tb_address a where v.scope_address_id=a.id and a.city=?1 and v.status=6 and v.scope=1 and v.source=0 and v.create_time>=?2 order by v.create_time desc",
      nativeQuery = true)
  List<Video> findCityLimit(String city, Date startDate);

  @Query(
      value = "select v.* from video v where v.video_id in (?1) order by create_time desc ",
      nativeQuery = true)
  List<Video> findByVideoList(List<String> videoId);

  @Query(
      value = "select v.* from video v where v.status=2 and v.create_time<?1",
      nativeQuery = true)
  List<Video> findCheck(Date startTime);

  @Query(
      value = "select v.* from video v where v.status=6 and v.create_time<?1 and width is null",
      nativeQuery = true)
  List<Video> findWithoutDimension(Date startDate);

  @Query(value = "select v.video_id from video v where v.status=6 and create_time>?1 and create_time<?2", nativeQuery = true)
  List<String> findAllVideoId(String startTime, String endTime);

  @Query(value = "select v.video_id from video v where v.status=6 limit ?1,?2", nativeQuery = true)
  List<String> findAllVideoIdPage(Integer pageNumber, Integer pageSize);

  @Query(
      value =
          "select v.* from video v where v.status=6 and v.source=0 and address_id=?1 order by create_time asc limit 1",
      nativeQuery = true)
  List<Video> findFirstVideo(Long addressId);

  @Query(
      value =
          "select v.* from video v where uid=?1 and v.status=6 and address_id=?2 order by create_time asc",
      nativeQuery = true)
  List<Video> landOwnerVideoList(String uid, Long addressId);

  @Query(
      value =
          "select v.* from video v where source=0 and v.status=6 and address_id=?1 and uid<>?2 order by create_time desc limit 1000",
      nativeQuery = true)
  List<Video> landOtherVideoList(Long addressId, String uid);

  @Query(
      value =
          "select * from video where source=0 and status=6 and uid=?1 and create_time>?3 and create_time<?4 order by score desc limit ?2",
      nativeQuery = true)
  List<Video> findTopScore(String uid, int top, Date startTime, Date endTime);

  @Query(
      value =
          "select * from video where source=0 and status=6 and score>0 and status_person=1 and uid=?1 and create_time>?3 and create_time<?4 order by create_time asc limit ?2",
      nativeQuery = true)
  List<Video> findFront(String uid, int top, Date startTime, Date endTime);

  @Query(
      value =
          "SELECT * FROM video WHERE heat = (SELECT MAX(heat) FROM video WHERE uid = ?1 AND activity_id = ?2)",
      nativeQuery = true)
  Video findOneHottest(String currentUid, Long activityId);

  /**
   * Author: Tablo
   *
   * <p>Description:[以热度降序分页查询活动视频] Created on 18:07 2019/12/14
   *
   * @param activityId 活动id
   * @param pageable 分页数据
   * @return java.util.List<com.quantumtime.qc.entity.feeds.Video>
   */
  @Query(value = "select v from Video v  where v.activityId =?1")
  List<Video> queryActVideo(Long activityId, Pageable pageable);

  /**
   * Author: Tablo
   *
   * <p>Description:[以热度降序分页查询活动视频] Created on 18:07 2019/12/14
   *
   * @param activityId 活动id
   * @return java.util.List<com.quantumtime.qc.entity.feeds.Video>
   */
  @Query(value = "select v from Video v  where v.activityId =?1")
  List<Video> queryActVideo(Long activityId);

  Video findByVideoId(String videoId);

  @Query(
          value =
                  "select * from qc.video where to_days(create_time) = to_days(now())and uid=?1 and status=6 AND activity_id = ?2",
          nativeQuery = true)
  List<Video> taskSendAct(String uid, Long activityId);
}
