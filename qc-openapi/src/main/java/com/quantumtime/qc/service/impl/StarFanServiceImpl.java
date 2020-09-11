package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.common.exception.ExpFunction;
import com.quantumtime.qc.entity.StarFan;
import com.quantumtime.qc.entity.User;
import com.quantumtime.qc.repository.StarFanRepository;
import com.quantumtime.qc.repository.UserRepository;
import com.quantumtime.qc.service.StarFanService;
import com.quantumtime.qc.vo.StarFanListVo;
import com.quantumtime.qc.wrap.AttentionResult;
import com.quantumtime.qc.wrap.RelationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.quantumtime.qc.common.constant.ErrorCodeConstant.NOT_FOLLOW;

/**
 * .Description:粉丝关系业务实现 Program:qc-api.Created on 2019-11-12 16:33
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 * @blame RD Team
 */
@Service
@Slf4j
public class StarFanServiceImpl implements StarFanService {

  @Resource private StarFanRepository starFanRepository;
  @Resource private UserRepository userRepository;

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean attention(User user, String starUid) {
    int blackResult = userRepository.getBlackResult(user.getUid(), starUid);
    ExpFunction.true4ThrowBiz(blackResult > 0, NOT_FOLLOW);
    if (user.getUid().equals(starUid)) {
      return false;
    }
    String fanUid = user.getUid();
    Map<String, User> userMap = users2Map(fanUid, starUid);
    User fanUser = userMap.get(fanUid);
    User starUser = userMap.get(starUid);
    AttentionResult result =
        isAttention(starFanRepository.findAttention(fanUid, starUid), fanUid, starUid);
    if (result.isNull()) {
      starFanRepository.save(
          StarFan.builder()
              .fanUid(fanUid)
              .starUid(starUid)
              .twoWay(false)
              .createTime(LocalDateTime.now())
              .build());
      attention2save(fanUser, starUser, true);
    } else if (!result.isAttention()) {
      starFanRepository.save(result.getStarFan().setTwoWay(true));
      attention2save(fanUser, starUser, true);
    }
    return true;
  }

  /**
   * Created on 16:14 2019/11/14 Author: Tablo. 奖励积分 main
   * <p>Description:[用户数据save]
   *
   * @param fanUser 粉丝用户
   * @param starUser 焦点用户
   */
  private void attention2save(User fanUser, User starUser, boolean subscribe) {
    int num4Fan = subscribe ? 1 : -1;
    fanUser.setStarSum(fanUser.getStarSum() + num4Fan);
    starUser.setFanSum(starUser.getFanSum() + num4Fan);
    userRepository.saveAll(Arrays.asList(fanUser, starUser));
  }

  /**
   * Created on 16:15 2019/11/14 Author: Tablo.
   *
   * <p>Description:[查询两个用户并以uid为key置于Map中]
   *
   * @param uid1 用户1
   * @param uid2 用户2
   * @return java.util.Map<java.lang.String,com.quantumtime.qc.entity.User>
   */
  private Map<String, User> users2Map(String uid1, String uid2) {
    return userRepository.findAllById(Arrays.asList(uid1, uid2)).stream()
        .collect(Collectors.toMap(User::getUid, user -> user, (a, b) -> b, () -> new HashMap<>(2)));
  }

  @Override
  public AttentionResult isAttention(StarFan exist, String fanUid, String starUid) {
    AttentionResult result = new AttentionResult().setStarFan(exist);
    return exist == null
        ? result.setAttention(false).setNull(true).setRelationCode(NO)
        : !exist.getTwoWay()
            ? exist.getFanUid().equals(fanUid)
                ? result.setAttention(true).setPositive(true).setRelationCode(FORWARD)
                : result.setAttention(false).setPositive(false).setRelationCode(REVERSE)
            : exist.getFanUid().equals(fanUid)
                ? result.setAttention(true).setPositive(true).setRelationCode(MUTUAL_CONCERN)
                : result.setAttention(true).setPositive(false).setRelationCode(MUTUAL_CONCERN);
  }

  @Override
  public int checkRelation(String fanUid, String starUid) {
    return fanUid.equals(starUid)
        ? 9
        : isAttention(find2Relation(fanUid, starUid), fanUid, starUid).getRelationCode();
  }

  private StarFan find2Relation(String fanUid, String starUid) {
    return starFanRepository.findAttention(fanUid, starUid);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean unsubscribe(String fanUid, String starUid) {
    if (fanUid.equals(starUid)) {
      return false;
    }
    Map<String, User> userMap = users2Map(fanUid, starUid);
    User fanUser = userMap.get(fanUid);
    User starUser = userMap.get(starUid);
    StarFan exist = starFanRepository.findAttention(fanUid, starUid);
    AttentionResult result = isAttention(exist, fanUid, starUid);
    StarFan starFan = result.getStarFan();
    if (result.isNull()) {
      return true;
    }
    if (starFan.getTwoWay()) {
      starFanRepository.save(
          result.isPositive()
              ? starFan.setFanUid(starUid).setStarUid(fanUid).setTwoWay(false)
              : starFan.setTwoWay(false));
      attention2save(fanUser, starUser, false);

    } else if (result.isPositive()) {
      starFanRepository.deleteById(starFan.getId());
      attention2save(fanUser, starUser, false);
    }
    return true;
  }

  public void removeStarFan(List<String> uidList){
    starFanRepository.findUserFanStar(uidList);
  }

  @Override
  public void unsubscribe2Way(String fanUid, String starUid) {
    Map<String, User> userMap = users2Map(fanUid, starUid);
    User fanUser = userMap.get(fanUid);
    User starUser = userMap.get(starUid);
    StarFan exist = starFanRepository.findAttention(fanUid, starUid);
    AttentionResult result = isAttention(exist, fanUid, starUid);
    if (result.isNull()) {
      return;
    }
    if (result.getRelationCode() == MUTUAL_CONCERN) {
      fanUser.setStarSum(fanUser.getStarSum() - 1).setFanSum(fanUser.getFanSum() - 1);
      starUser.setFanSum(starUser.getFanSum() - 1).setStarSum(starUser.getStarSum() - 1);
    } else if (result.getRelationCode() == FORWARD) {
      fanUser.setStarSum(fanUser.getStarSum() - 1);
      starUser.setFanSum(starUser.getFanSum() - 1);
    } else {
      fanUser.setFanSum(fanUser.getFanSum() - 1);
      starUser.setStarSum(starUser.getStarSum() - 1);
    }
    userRepository.saveAll(Arrays.asList(fanUser, starUser));
    starFanRepository.deleteById(exist.getId());
  }

  @Override
  public int relation(StarFan starFan, String fanUid, String thisUid) {
    int relationType = NO;
    if (starFan == null) {
      return relationType;
    } else if (starFan.getTwoWay()) {
      relationType = MUTUAL_CONCERN;
    } else if (starFan.getFanUid().equals(fanUid)) {
      relationType = FORWARD;
    } else if (starFan.getStarUid().equals(fanUid)) {
      relationType = REVERSE;
    }
    return relationType;
  }

  @Override
  public List<Map<String, Integer>> starFanList(
      StarFanListVo starFanListVo) { // List<String> uidList, String thisUid
    List temp = new ArrayList<HashMap>();
    Map<String, Integer> map = new HashMap();
    List<String> uidList = starFanListVo.getUidList();
    String thisUid = starFanListVo.getThisUid();
    List<StarFan> sfList =
        uidList.isEmpty()
            ? Collections.emptyList()
            : starFanRepository.starFanList(uidList, thisUid);

    // 全部为毫无关系
    if (sfList.isEmpty()) {
      for (String starFan : uidList) {
        map.put(starFan, NO);
      }
    } else {
      for (int i = 0; i <= sfList.size() - 1; i++) {
        if (sfList.get(i).getTwoWay()) {
          // 互相关注如果fanUid为thisUid 则put StarUid
          if (sfList.get(i).getStarUid().equals(thisUid)) {
            map.put(sfList.get(i).getFanUid(), MUTUAL_CONCERN);

          } else if (sfList.get(i).getFanUid().equals(thisUid)) {
            map.put(sfList.get(i).getStarUid(), MUTUAL_CONCERN);
          }

        } else if (sfList.get(i).getStarUid().equals(thisUid)) { // 被关注者id是thisid的话
          map.put(sfList.get(i).getFanUid(), REVERSE);
        } else if (sfList.get(i).getFanUid().equals(thisUid)) { // 已经关注了toid
          map.put(sfList.get(i).getStarUid(), FORWARD);
        }
      }
      // 如果查出来的Starfanlist的size比传入的uidlist的size小 无uid对应的Starfan 的关系为NO
      if (sfList.size() < uidList.size()) {
        List<String> mapUid = new ArrayList<>();
        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
          mapUid.add(String.valueOf(iter.next()));
        }

        List<String> list = new ArrayList<>(uidList);

        for (int i = 0; i < mapUid.size(); i++) {
          if (!list.contains(mapUid.get(i))) {
            list.add(mapUid.get(i));
          }
        }
        for (int i = 0; i < list.size(); i++) {
          if (uidList.contains(list.get(i)) && mapUid.contains(list.get(i))) {
            list.remove(i);
            i--;
          }
        }
        for (String uid : list) {
          map.put(uid, NO);
        }
      }
    }
    temp.add(map);

    return temp;
  }

  @Override
  public Page<StarFan> page4Stars(String fanUid, Integer pageNum, Integer size) {
    return starFanRepository.findStarsByPage(
        fanUid, PageRequest.of(pageNum - 1, size, new Sort(Sort.Direction.DESC, "createTime")));
  }

  @Override
  public Page<StarFan> page4Fans(String starUid, Integer pageNum, Integer size) {
    return starFanRepository.findFansByPage(starUid, PageRequest.of(pageNum - 1, size));
  }

  @Override
  public List<RelationInfo> getStarUsers(
      String currentUid, String starUid, Integer pageNum, Integer size) {
    // 分页查询此人订阅人的用户Id
    List<String> starIds4Other =
        page4Stars(starUid, pageNum, size).getContent().stream()
            .map(starFan -> getOtherUid(starFan, starUid))
            .collect(Collectors.toList());
    return buildRelationships(starIds4Other, currentUid);
  }

  @Override
  public List<RelationInfo> getFanUsers(
      String currentUid, String starUid, Integer pageNum, Integer size) {
    // 分页查询除此人的粉丝用户Uid
    List<String> fanIds4Other =
        page4Fans(starUid, pageNum, size).getContent().stream()
            .map(starFan -> getOtherUid(starFan, starUid))
            .collect(Collectors.toList());
    return buildRelationships(fanIds4Other, currentUid);
  }

  /**
   * Created on 20:00 2019/11/19 Author: Tablo.
   *
   * <p>Description:[构建关系结果List]
   *
   * @param userIds 用户Id
   * @param currentUid 我的Id
   * @return java.util.List<com.quantumtime.qc.wrap.RelationInfo> 我和用户的关系及必要数据
   */
  private List<RelationInfo> buildRelationships(List<String> userIds, String currentUid) {
    if (userIds.isEmpty()) {
      return Collections.emptyList();
    }
    // 查询这些Uid对应的用户并以uid为key，用户对象为value存放于HashMap
    HashMap<String, User> userMap =
        userRepository.findByUidList(userIds).stream()
            .collect(Collectors.toMap(User::getUid, user -> user, (a, b) -> b, HashMap::new));
    userIds = userIds.stream().filter(userMap::containsKey).collect(Collectors.toList());
    // 以uid为key，将用户信息转化为需要的展示对象默认Vo->value，默认关系为不存在，存于我的关系Map中
    Map<String, RelationInfo> collect = queryRelation(userIds, userMap, currentUid);
    return userIds.stream().map(collect::get).collect(Collectors.toList());
  }

  @Override
  public Map<String, RelationInfo> queryRelation(
      List<String> userIds, Map<String, User> userMap, String currentUid) {
    if (userIds.isEmpty()) {
      return Collections.emptyMap();
    }
    // 以uid为key，将用户信息转化为需要的展示对象默认Vo->value，默认关系为不存在，存于我的关系Map中
    HashMap<String, RelationInfo> collect = new HashMap<>(16);
    userIds.forEach(
        uid ->
            Optional.ofNullable(userMap.get(uid))
                .ifPresent(user -> collect.put(uid, RelationInfo.convert2User(user))));
    // 如若用户登陆返回关系，未登陆直接返回
    Optional.ofNullable(currentUid)
        .ifPresent(
            current ->
                // 查询这一批人与我的关系，通过条件判断，把对方的uid置为key，关系对象为value，放置于关系map中
                starFanRepository.starFanList(userIds, current).stream()
                    .collect(
                        Collectors.toMap(
                            starFan -> getOtherUid(starFan, current),
                            starFan -> starFan,
                            (a, b) -> b,
                            HashMap::new))
                    // 遍历关系map，判断关系并设置入collect中
                    .forEach(
                        (key, value) ->
                            collect.get(key).setRelationCode(relation(value, current, key))));
    return collect;
  }
  /**
   * Created on 19:51 2019/11/19 Author: Tablo.
   *
   * <p>Description:[从判断包含此人的关系中获取对方的Uid]
   *
   * @param starFan 此人关系
   * @param currentUid 此人Id
   * @return java.lang.String 返回别人的Id
   */
  private String getOtherUid(StarFan starFan, String currentUid) {
    return Objects.equals(starFan.getFanUid(), currentUid)
        ? starFan.getStarUid()
        : starFan.getFanUid();
  }
}
