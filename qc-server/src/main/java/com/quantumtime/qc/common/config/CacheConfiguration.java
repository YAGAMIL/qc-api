package com.quantumtime.qc.common.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.quantumtime.qc.common.config.MyRedisCacheManager.JACKSON_PAIR;
import static com.quantumtime.qc.common.config.MyRedisCacheManager.STRING_PAIR;

/**
 * .Description:缓存配置 & Created on 2019/10/18 16:38
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {

    /** key serializer */
    @SuppressWarnings("WeakerAccess")
    public static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();
    /**
     * value serializer
     *
     * <pre>
     *     使用 FastJsonRedisSerializer 会报错：java.lang.ClassCastException
     *     FastJsonRedisSerializer<Object> fastSerializer = new FastJsonRedisSerializer<>(Object.class);
     * </pre>
     */
    @SuppressWarnings("WeakerAccess")
    public static final GenericFastJsonRedisSerializer FAST_JSON_SERIALIZER = new GenericFastJsonRedisSerializer();

    @SuppressWarnings("WeakerAccess")
    public static final GenericJackson2JsonRedisSerializer JACKSON_SERIALIZER =
            new GenericJackson2JsonRedisSerializer();

    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(JACKSON_SERIALIZER);
        template.setHashValueSerializer(JACKSON_SERIALIZER);
        template.setKeySerializer(STRING_SERIALIZER);
        template.setHashKeySerializer(STRING_SERIALIZER);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(JACKSON_PAIR)
                .serializeKeysWith(STRING_PAIR);
        // 设置缓存的默认过期时间，也是使用Duration设置
        config = config.entryTtl(Duration.ofHours(1)).disableCachingNullValues();
        // 不缓存空值
        RedisCacheConfiguration config2 = config.entryTtl(Duration.ofMinutes(10)).disableCachingNullValues();
        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("cache-user");
        cacheNames.add("cache-information");
        cacheNames.add("cache-theme");
        cacheNames.add("cache-errorCode");
        cacheNames.add("cache-setting");
        cacheNames.add("cache-address");
        cacheNames.add("cache-business");
        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>(6);
        configMap.put("cache-user", config);
        configMap.put("cache-information", config);
        configMap.put("cache-theme", config);
        configMap.put("cache-errorCode", config2);
        configMap.put("cache-setting", config2);
        configMap.put("cache-address", config);
        configMap.put("cache-business", config);
        // 使用自定义的缓存配置初始化一个cacheManager
        return MyRedisCacheManager.builder(factory)
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();
    }
}
