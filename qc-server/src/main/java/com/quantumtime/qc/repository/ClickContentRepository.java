package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.ClickContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Description:点击事件持久层 Created on 2019/12/10 14:26
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Repository
public interface ClickContentRepository extends JpaRepository<ClickContent, Long> {

    /**
     * Gets click records.
     *
     * @param clickType the click type
     * @param contentIds the content ids
     * @param contentType the content type
     * @param createUid the create uid
     * @return the click records
     */
    @Query(
            value =
                    "select cr from ClickContent as cr where cr.clickTypeId=?1 and cr.contentId in ?2 and cr.contentType=?3 and cr.createUid=?4")
    List<ClickContent> getClickRecords(Integer clickType, List<Long> contentIds, Integer contentType, String createUid);

    /**
     * Click content list.
     *
     * @param create_uid the create uid
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return the list
     */
    @Query(
            value =
                    "select s.* from click_content as s  where s.create_uid = ?1 and s.click_type_id=0 order by create_time desc limit ?2,?3",
            nativeQuery = true)
    List<ClickContent> clickContent(String create_uid, Integer pageNumber, Integer pageSize);

    /**
     * Click content sum integer.
     *
     * @param content_id the content id
     * @param clickTypeID the click type id
     * @return the integer
     */
    @Query(
            value =
                    "select  count(*)  from click_content as s where s.content_type=1 and s.content_id=?1 and s.click_type_id =?2",
            nativeQuery = true)
    Integer clickContentSum(Long content_id, Integer clickTypeID);

    /**
     * Find click contents by content id is in and content type list.
     *
     * @param contentIds the content ids
     * @param contentType the content type
     * @return the list
     */
    List<ClickContent> findClickContentsByContentIdIsInAndContentType(List<Long> contentIds, Integer contentType);

    /**
     * Find click contents by content id is in and content type list.
     *
     * @param contentIds the content ids
     * @param contentType the content type
     * @return the list
     */
    List<ClickContent> findClickContentsByContentIdIsInAndContentType(Long contentIds, Integer contentType);

    /**
     * Feeds click like click content.
     *
     * @param feedsId the feeds id
     * @param uid the uid
     * @return the click content
     */
    @Query(
            value =
                    "select s.* from click_content as s where s.content_type=1 and s.click_type_id=0 and s.content_id=?1 and s.create_uid=?2",
            nativeQuery = true)
    ClickContent feedsClickLike(Long feedsId, String uid);

    /**
     * Video like sum integer.
     *
     * @param videoId the video id
     * @return the integer
     */
    @Query(
            value =
                    "select  count(*)  from click_content as s where s.content_type=2 and s.content_id=?1 and s.click_type_id=0",
            nativeQuery = true)
    Integer videoLikeSum(String videoId);

    /**
     * Video click sum list.
     *
     * @param videoId the video id
     * @return the list
     */
    @Query(
            value =
                    "select click_type_id, count(*)  from click_content as s where s.content_type=2 and s.content_id=?1 group by click_type_id",
            nativeQuery = true)
    List<Object> videoClickSum(String videoId);

    /**
     * Video user like click content.
     *
     * @param videoId the video id
     * @param uid the uid
     * @return the click content
     */
    @Query(
            value =
                    "select  *  from click_content as s where s.content_type=2 and s.content_id=?1 and s.create_uid =?2 and s.click_type_id=0 ",
            nativeQuery = true)
    ClickContent videoUserLike(String videoId, String uid);

    /**
     * Video like num list.
     *
     * @param videoIds the video ids
     * @return the list
     */
    @Query(
            value =
                    "select s.content_id, count(*)  from click_content as s where s.content_type=2 and s.content_id in ?1 and s.click_type_id=0 group by s.content_id",
            nativeQuery = true)
    List<Object> videoLikeNum(List<String> videoIds);

    /**
     * Created on 15:43 2019/11/23 Author: Tablo.
     *
     * <p>Description:[查询视频的查看数]
     *
     * @param videoIds 视频Id集合
     * @return java.util.List<java.lang.Object> list
     */
    @Query(
            value =
                    "select s.content_id, count(*)  from click_content as s where s.content_type=2 and s.content_id in ?1 and s.click_type_id=1 group by s.content_id",
            nativeQuery = true)
    List<Object> videoViewNum(List<String> videoIds);

    /**
     * Video user like list.
     *
     * @param videoIds the video ids
     * @param uid the uid
     * @return the list
     */
    @Query(
            value =
                    "select *  from click_content as s where s.content_type=2 and s.content_id in ?1 and s.click_type_id=0 and s.create_uid =?2",
            nativeQuery = true)
    List<ClickContent> videoUserLike(List<String> videoIds, String uid);

    /**
     * User like sum integer.
     *
     * @param uid the uid
     * @return the integer
     */
    @Query(
            value =
                    "select count(*) from video v where v.video_id in ( select s.content_id from click_content  s where s.content_type=2 and s.click_type_id=0 and s.create_uid =?1 ) and v.status>=6 ",
            nativeQuery = true)
    Integer userLikeSum(String uid);

    /**
     * User like videos list.
     *
     * @param uid the uid
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return the list
     */
    @Query(
            value =
                    "select s.content_id from click_content as s where s.content_type=2 and s.click_type_id=0 and s.create_uid =?1 order by s.create_time desc limit ?2,?3",
            nativeQuery = true)
    List<String> userLikeVideos(String uid, Integer pageNumber, Integer pageSize);

    /**
     * Find all lick in list.
     *
     * @param likes the likes
     * @param videoId the video id
     * @return the list
     */
    @Query(
            value =
                    "select  * from qc.click_content where click_type_id = 0 AND create_uid in ?1 AND content_id = ?2 AND  content_type = 2",
            nativeQuery = true)
    List<ClickContent> findAllLickIn(List<String> likes, String videoId);

    @Query(
            value =
                    "select  count(*) from qc.click_content where click_type_id = 0 AND content_type = 2 AND content_id = ?1  ",
            nativeQuery = true)
    int findClickSumByVideo(String videoId);

    @Query(
            value =
                    "select s.content_id from click_content  s where s.content_type=2 and s.click_type_id=0 and s.create_uid =?1 order by create_time desc limit ?2,?3  ",
            nativeQuery = true)
    List<String> clickSum(String uid, Integer pageNumber, Integer pageSize);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM click_content WHERE create_uid IN(SELECT t_uid FROM tb_user WHERE t_is_vest = TRUE)  ",
            nativeQuery = true)
    int removeRobot();


    @Query(
            value =
                    "select * from click_content where to_days(create_time) = to_days(now())and create_uid=?1 and content_type=2 and click_type_id=0  ",
            nativeQuery = true)
    List<ClickContent> taskClick(String uid);
}
