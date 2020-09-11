package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.StarFan;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.vo.StarFanListVo;
import com.quantumtime.qc.wrap.AttentionResult;
import com.quantumtime.qc.wrap.RelationInfo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * .Description:粉丝关系业务层接口 Program:qc-api.Created on 2019-11-12 16:31
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface StarFanService {

    /** 无关注关系 */
    int NO = 0;
    /** 我关注了他，他没关注我 */
    int FORWARD = 1;
    /** 互相关注 */
    int MUTUAL_CONCERN = 2;
    /** 他关注了我，我没关注他 */
    int REVERSE = 3;
    /**
     * Created on 19:33 2019/11/12 Author: Tablo.
     *
     * <p>Description:[关注行为]
     *
     * @param user 操作用户，粉丝
     * @param starUid 被关注Uid
     * @return boolean boolean
     */
    boolean attention(User user, String starUid);

    /**
     * Created on 19:34 2019/11/12 Author: Tablo.
     *
     * <p>Description:[是否关注，根据返回对象其中Attention字段]
     *
     * @param exist 已存在的关系
     * @param fanUid 粉丝Uid
     * @param starUid 被关注Uid
     * @return com.quantumtime.qc.wrap.AttentionResult attention result
     */
    AttentionResult isAttention(StarFan exist, String fanUid, String starUid);

    /**
     * Created on 12:00 2019/11/16 Author: Tablo.
     *
     * <p>Description:[检验用户关系，返回用户关系code，本人账户返回9]
     *
     * @param fanUid 粉丝用户
     * @param starUid 被关注用户
     * @return int int
     */
    int checkRelation(String fanUid, String starUid);

    /**
     * Created on 15:35 2019/11/13 Author: Tablo.
     *
     * <p>Description:[取消对用户的关注]
     *
     * @param fanUid 粉丝Id，当前用户
     * @param starUid 被关注者
     * @return boolean boolean
     */
    boolean unsubscribe(String fanUid, String starUid);

    /**
     * Author: Tablo
     *
     * <p>Description:[删除用户关系] Created on 14:22 2019/12/10
     *
     * @param fanUid 粉丝UID
     * @param starUid 被关注Uid
     */
    void unsubscribe2Way(String fanUid, String starUid);

    /**
     * Created on 15:35 2019/11/13 Author: dong.
     *
     * <p>Description:[查询两个用户之间的关系]
     *
     * @param starFan the star fan
     * @param toUid 目标用户的uid
     * @param thisUid 当前登录用户
     * @return int 关系状态
     */
    int relation(StarFan starFan, String toUid, String thisUid);

    /**
     * Created on 15:35 2019/11/13 Author: dong.
     *
     * <p>Description:[查询一批uid 和一个uid之间的关系]
     *
     * @param starFanListVo the star fan list vo
     * @return List<Map < String, Integer> > 返回uidlist中的每个uid对应 一个uid
     */
    List<Map<String, Integer>> starFanList(StarFanListVo starFanListVo);

    /**
     * Page 4 stars page. 分页查询受阅人
     *
     * @param fanUid the fan uid
     * @param pageNum the page num
     * @param size the size
     * @return the page
     */
    Page<StarFan> page4Stars(String fanUid, Integer pageNum, Integer size);

    /**
     * Created on 20:00 2019/11/19 Author: Tablo.
     *
     * <p>Description:[分页查询粉丝]
     *
     * @param starUid the star uid
     * @param pageNum the page num
     * @param size the size
     * @return the page
     */
    Page<StarFan> page4Fans(String starUid, Integer pageNum, Integer size);

    /**
     * Created on 20:00 2019/11/19 Author: Tablo.
     *
     * <p>Description:[获取别人的关注用户并带上与当前用户的关系]
     *
     * @param currentUid the current uid 我的Id
     * @param starUid the star uid 我查看的人的Id
     * @param pageNum the page num
     * @param size the size
     * @return the star users 最终结果，他关注的人们与我的关系
     */
    List<RelationInfo> getStarUsers(String currentUid, String starUid, Integer pageNum, Integer size);

    /**
     * Created on 20:00 2019/11/19 Author: Tablo.
     *
     * <p>Description:[获取别人的粉丝用户，并附带与当前用户的关系]
     *
     * @param currentUid 我的Id
     * @param starUid 我查看的人的Id
     * @param pageNum the page num
     * @param size the size
     * @return the fan 最终结果，他的粉丝们与我的关系
     */
    List<RelationInfo> getFanUsers(String currentUid, String starUid, Integer pageNum, Integer size);

    /**
     * 根据用户map批量查询关系，以uid为key，value为关系Info
     *
     * @param userIds the user ids
     * @param userMap the user map
     * @param currentUid the current uid
     * @return the map
     */
    Map<String, RelationInfo> queryRelation(List<String> userIds, Map<String, User> userMap, String currentUid);
}
