package com.quantumtime.qc;

import com.quantumtime.qc.service.ActivityService;
import com.quantumtime.qc.service.impl.activity.VideoHeatService;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.quantumtime.qc.common.constant.RedisConstant.ACTIVE_CACHE_KEY;

/**
 * Description: Program:qc-api Created on 2019-12-19 15:58
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public class HeatTest extends QCApplicationTests {
    @Resource
    VideoHeatService videoHeatService;

    @Resource
    ActivityService activityService;

    @Resource
    RedisTemplate<String, String> redisTemplate;



    @Test
    public void makeHeatRedis() {
        long activityId = 1L;
        videoHeatService.makeHeat(activityId);
        Set<ZSetOperations.TypedTuple<String>> zSetValue =
                redisTemplate.opsForZSet().reverseRangeWithScores(ACTIVE_CACHE_KEY + activityId, 0, 100);
        Map<String, Double> result = Objects.requireNonNull(zSetValue).stream()
                .collect(Collectors.toMap(
                        ZSetOperations.TypedTuple::getValue,
                        stringTypedTuple -> Optional.ofNullable(stringTypedTuple.getScore()).orElse(0d),
                        (a, b) -> b));
        System.out.println("通过add(K key, Set<ZSetOperations.TypedTuple<V>> tuples)方法添加元素:" + zSetValue);
        Long rank1 = redisTemplate.opsForZSet().reverseRank(ACTIVE_CACHE_KEY + activityId, "3de4fa7a2de840db96f856b1814fec70");
        Long rank2 = redisTemplate.opsForZSet().reverseRank(ACTIVE_CACHE_KEY + activityId, "cd8d1b3a3f8242ec9c9fa18cdd9c3a4b");
        Long rank3 = redisTemplate.opsForZSet().reverseRank(ACTIVE_CACHE_KEY + activityId, "7bb7e7f68b584871adeacf3a8b0b2955");
        System.err.println("rank1------"+rank1 + "rank2------"+ rank2 + "rank3------" + rank3);
        System.err.println(result);
    }
    @Test
    public void activeTest() {
//        List<Long> list = activityService.queryActive(LocalDateTime.now());
//        System.err.println(list);
        videoHeatService.sortHeatVideos();
    }
}
