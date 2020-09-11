package com.quantumtime.qc.service.impl.activity;

import com.quantumtime.qc.entity.activity.Activity;
import com.quantumtime.qc.entity.activity.HeatRule;
import com.quantumtime.qc.entity.feeds.Video;
import com.quantumtime.qc.repository.VideoRepository;
import com.quantumtime.qc.service.ActivityService;
import com.quantumtime.qc.service.ClickContentService;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.quantumtime.qc.common.constant.RedisConstant.ACTIVE_CACHE_KEY;
import static com.quantumtime.qc.common.utils.GlobalUtils.getNullNumElse;

/**
 * Description:视频热度 Program:qc-api Created on 2019-12-17 15:56
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Service
@Slf4j
class VideoHeatServiceImpl implements VideoHeatService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private VideoRepository videoRepository;

    @Resource
    private ClickContentService clickContentService;

    @Resource
    private HeatRuleService heatRuleService;

    @Resource
    private ActivityService activityService;

    @Override
    public void sortHeatVideos() {
        activityService.queryActive(LocalDateTime.now()).stream().map(Activity::getId).forEach(this::makeHeat);
    }

    @Override
    public void makeHeat(Long activityId) {
        heatRuleService.matchActRul(activityId).ifPresent(this::doMakeHeat);
        XxlJobLogger.log("活动ID :  -------"+activityId);
    }

    /**
     * 热度= （播放量× 系数+点赞量×系数 +分享量×系数 +主题相关性× 系数 +视频分数× 系数）/系数和 。未勾选的，不参与计算 。 取整数 。30分钟更新数据。 Author: Tablo
     *
     * <p>Description:[] Created on 16:27 2019/12/18
     *
     * @param heatRule 规则
     */
    @Override
    public void doMakeHeat(HeatRule heatRule) {
        List<Video> videos = videoRepository.queryActVideo(heatRule.getActivityId());
        if (videos.isEmpty()) {
            return;
        }
        List<String> videoIds = videos.stream().map(Video::getVideoId).collect(Collectors.toList());
        Map<String, Integer> likeMap = clickContentService.getClickMap(videoIds, true);
        Map<String, Integer> viewMap = clickContentService.getClickMap(videoIds, false);
        Set<ZSetOperations.TypedTuple<String>> tuples = videos.stream().map(i -> new DefaultTypedTuple<>(i.getVideoId(), setHeat(heatRule.initNullable(), likeMap, viewMap, i).getHeat() * 1.0)).collect(Collectors.toSet());
        redisTemplate.opsForZSet().add(ACTIVE_CACHE_KEY + heatRule.getActivityId(), tuples);
        redisTemplate.expire(ACTIVE_CACHE_KEY + heatRule.getActivityId(), 1, TimeUnit.DAYS);
        XxlJobLogger.log("活动ID："+heatRule.getActivityId(),"参与视频"+videoIds.size());
        videoRepository.saveAll(videos);
    }

    private Video setHeat(HeatRule heatRule, Map<String, Integer> likeMap, Map<String, Integer> viewMap, Video video) {
        String videoId = video.getVideoId();
        Integer viewSum = getNullNumElse(viewMap.get(videoId));
        Integer likeSum = getNullNumElse(likeMap.get(videoId));
        int i = (viewSum * heatRule.getView()
                        + likeSum * heatRule.getLike()
                        + getNullNumElse(video.getShares()) * heatRule.getShare()
                        + getNullNumElse(video.getRelated()) * heatRule.getRelated()
                        + getNullNumElse(video.getSource()) * heatRule.getScore())
                / heatRule.getCoefficientSum();
        return video.setHeat(i);
    }
}
