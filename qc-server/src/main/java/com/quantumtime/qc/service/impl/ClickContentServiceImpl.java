package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.entity.ClickContent;
import com.quantumtime.qc.entity.feeds.Article;
import com.quantumtime.qc.entity.feeds.Feeds;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.repository.ClickContentRepository;
import com.quantumtime.qc.repository.UserRepository;
import com.quantumtime.qc.repository.VideoRepository;
import com.quantumtime.qc.service.ClickContentService;
import com.quantumtime.qc.service.FeedsService;
import com.quantumtime.qc.service.IArticleService;
import com.quantumtime.qc.vo.ClickContentVo;
import com.quantumtime.qc.vo.RobotClickVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Slf4j
public class ClickContentServiceImpl implements ClickContentService {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    IArticleService iArticleService;
    @Autowired
    FeedsService feedsService;
    @Autowired
    private ClickContentRepository clickContentRepository;
    @Autowired
    private AccountHelp accountHelp;
    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static ClickContent buildClickContent(String uid, int clickType, String contentId) {
        return new ClickContent()
                .setClickTypeId(clickType)
                .setContentType(2)
                .setCreateUid(uid)
                .setContentId(contentId)
                .setCreateTime(LocalDateTime.now());
    }

    public static int randomMax(int max) {
        return new Random().nextInt(max);
    }

    public static int resultSum(int likeSum) {

        return (likeSum * FINAL_MULTIPLES + EXTRA) + LIKE_FACTOR;
    }

    public static ClickContent copy2View(ClickContent clickContent) {
        return new ClickContent()
                .setContentId(clickContent.getContentId())
                .setClickTypeId(1)
                .setContentType(clickContent.getContentType())
                .setCreateUid(clickContent.getCreateUid())
                .setCreateTime(clickContent.getCreateTime());
    }

    @Override
    public Map<String, Integer> getClickMap(List<String> videoIds, boolean isLike) {
        List<Object> clickList =
                isLike ? clickContentRepository.videoLikeNum(videoIds) : clickContentRepository.videoViewNum(videoIds);
        Map<String, Integer> viewMap = new HashMap<>(16);
        if (!clickList.isEmpty()) {
            clickList.forEach(object -> {
                Object[] ary = (Object[]) object;
                viewMap.put(ary[0].toString(), Integer.parseInt(ary[1].toString()));
            });
        }
        return viewMap;
    }
    @Override
    public List<ClickContent> getRecords(
            Integer clickType, List<Long> contentIds, Integer contentType, String createUid) {
        return clickContentRepository.getClickRecords(clickType, contentIds, contentType, createUid);
    }

    @Override
    public List<ClickContentVo> myRecords(Integer pageNumber, Integer pageSize) {
        Integer sizes = pageSize * (pageNumber - 1);
        String uid = accountHelp.getCurrentUser().getUid();
        List<ClickContent> list = clickContentRepository.clickContent(uid, sizes, pageSize);
        List<ClickContentVo> returnlist = new ArrayList<>();
        List<Long> feedsIdList = new ArrayList<>();
        List<Long> articleList = new ArrayList<>();
        for (ClickContent flows : list) {
            if (flows.getContentType() == 1) {
                feedsIdList.add(Long.valueOf(flows.getContentId()));
            } else {
                articleList.add(Long.valueOf(flows.getContentId()));
            }
        }
        List<Feeds> feedsDatilList = feedsService.findAllByIds(feedsIdList);
        List<Article> articlesDatilList = iArticleService.findAllByIds(articleList);
        for (int i = 0; i < list.size(); i++) {
            ClickContent clickContend = list.get(i);
            if (clickContend.getContentType() == 1) {
                for (Feeds feeds : feedsDatilList) {
                    if (clickContend.getContentId().equals(feeds.getFeeds_id())) {
                        ClickContentVo objectVo = new ClickContentVo();
                        objectVo.setObject(feeds);
                        objectVo.setType(1);
                        returnlist.add(objectVo);
                    }
                }
            } else {
                for (Article article : articlesDatilList) {
                    if (clickContend.getContentId().equals(article.getArticleId())) {
                        ClickContentVo objectVo = new ClickContentVo();
                        objectVo.setObject(article);
                        objectVo.setType(0);
                        returnlist.add(objectVo);
                    }
                }
            }
        }
        return returnlist;
    }

    @Override
    public List<ClickContent> robotClickOne(List<ClickContent> result, String videoId, int handleLimit) {
        List<String> robotUid = userRepository.robotUidList();
        int likeSum = clickContentRepository.findClickSumByVideo(videoId);
        if (likeSum > handleLimit) {
            return new ArrayList<>();
        }
        int count = getLastLikes(likeSum);
        int viewSum = (10 + randomMax(6)) * count;
        if (count > robotUid.size()) {
            count = robotUid.size() - 1;
        }
        List<String> viewUidList = robotViewSum(viewSum, robotUid);
        List<String> likeList = randomLikeSum(count, robotUid);
        List<ClickContent> likeResult = autoLike(likeList, videoId, likeSum, handleLimit);
        List<ClickContent> viewResult = autoView(viewUidList, videoId);
        result.addAll(likeResult);
        result.addAll(viewResult);
        return result;
    }

    @Override
    public Boolean removeRobot() {
        clickContentRepository.removeRobot();
        return true;
    }

    @Override
    public RobotClickVo robotClick(int handleLimit) {
        RobotClickVo robotClickVo = new RobotClickVo();
        long startTime = System.currentTimeMillis();
        long delta = 24L * 3600 * 1000;
        Date yesterday = new Date(System.currentTimeMillis() - delta);
        String yesterdayStr = sdf.format(yesterday);
        String yesterdayStartTimeStr = yesterdayStr + " 00:00:00";
        String yesterdayEndTimeStr = yesterdayStr + " 23:59:59";
        List<String> allVideoId = videoRepository.findAllVideoId(yesterdayStartTimeStr, yesterdayEndTimeStr);
        List<ClickContent> result = new ArrayList<>();
        int count = 0;
        for (String s : allVideoId) {
            result = robotClickOne(result, s, handleLimit);
            if (result.size() > 0) {
                if (count % 100 == 0) {
                    log.info("robotClick:" + count);
                }
                count++;
                batchSave(result);
            }
            result = new ArrayList<>();
        }
        log.info("total robotClick:" + count);
        long endTime = System.currentTimeMillis();
        long usingTime = endTime - startTime;
        robotClickVo.setRow(count);
        robotClickVo.setUsingTime(usingTime);
        return robotClickVo;
    }

    private List<String> randomLikeSum(int count, List<String> robotUid) {
        Random random = new Random();
        List<Integer> tempList = new ArrayList<>();
        List<String> likeList = new ArrayList<>();
        int temp;
        for (int i = 0; i < count; i++) {
            // 将产生的随机数作为被抽list的索引
            temp = random.nextInt(robotUid.size());
            if (!tempList.contains(temp)) {
                tempList.add(temp);
                likeList.add(robotUid.get(temp));
            } else {
                i--;
            }
        }
        return likeList;
    }

    private List<String> robotViewSum(int viewSum, List<String> robotUid) {
        List<String> viewList = new ArrayList<String>();
        int o = 0;
        for (int k = 0; k < viewSum; k++) {
            if (k < robotUid.size()) {
                viewList.add(robotUid.get(k));
            } else {
                if (o < robotUid.size()) {
                    viewList.add(robotUid.get(o));
                    o++;
                } else {
                    o = 0;
                }
            }
        }
        return viewList;
    }

    @Override
    public List<String> randomUid(int likeSum, String contentId) {

        likeSum = likeSum == 0 ? 1 : likeSum;
        int lastSum = likeSum * 19 + new Random().nextInt(18);
        List list = redisTemplate.opsForSet().randomMembers("", 19L);

        return null;
    }

    public List<ClickContent> autoLike(List<String> uidList, String contentId, int likeSum, int handleLimit) {
        if (likeSum > handleLimit) {
            return Collections.emptyList();
        }
        Map<String, List<ClickContent>> collect = clickContentRepository.findAllLickIn(uidList, contentId).stream()
                .collect(Collectors.groupingBy(ClickContent::getCreateUid));
        // 过滤掉已经点过赞的用户
        List<String> likeUidList =
                uidList.stream().filter(key -> !collect.containsKey(key)).collect(Collectors.toList());

        List<ClickContent> likes = likeUidList.stream()
                .map(uid -> buildClickContent(uid, 0, contentId))
                .collect(Collectors.toList());
        // 点赞的需要再查看一遍
        List<ClickContent> likes2View =
                likes.stream().map(ClickContentServiceImpl::copy2View).collect(Collectors.toList());
        return Stream.of(likes, likes2View).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public int getLastLikes(int currentLikeSum) {
        // 判断点赞数是否为0，为0则设置为1
        currentLikeSum = currentLikeSum == 0 ? 1 : currentLikeSum;
        // 获取最终的点赞总数为现有的点赞数19+（0~18）以内的随机数
        return currentLikeSum * FINAL_MULTIPLES + randomMax(EXTRA);
    }

    public int getLastViews(int lastLike) {
        return lastLike * (8 + randomMax(7));
    }

    public List<ClickContent> autoView(List<String> uidList, String contentId) {
        return uidList.stream().map(uid -> buildClickContent(uid, 1, contentId)).collect(Collectors.toList());
    }

    public List<String> expandList(List<String> list, int lastSum) {
        int initSize = list.size();
        int i = lastSum / initSize;
        int num = lastSum % initSize;
        List<String> medium = new ArrayList<>();
        IntStream.range(0, i).mapToObj(j -> list).forEach(medium::addAll);
        medium.addAll(list.subList(0, num));
        return medium;
    }

    @Override
    public void batchSave(List<ClickContent> result) {
        SqlParameterSource[] beanSources = SqlParameterSourceUtils.createBatch(result);
        String sql =
                "INSERT INTO click_content (click_type_id, content_id, content_type, create_time, create_uid, to_uid) VALUES (:clickTypeId,:contentId,:contentType,:createTime, :createUid, :toUid)";
        namedParameterJdbcTemplate.batchUpdate(sql, beanSources);
    }
}
