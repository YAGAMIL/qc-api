package com.quantumtime.qc.common.config;

import com.quantumtime.qc.common.annotation.CacheExpire;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

import static com.quantumtime.qc.common.config.CacheConfiguration.FAST_JSON_SERIALIZER;
import static com.quantumtime.qc.common.config.CacheConfiguration.JACKSON_SERIALIZER;
import static com.quantumtime.qc.common.config.CacheConfiguration.STRING_SERIALIZER;

/**
 * .Description:处理@Cacheable异常 Program:qc-api.Created on 2019-10-17 14:57
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Slf4j
public class MyRedisCacheManager extends RedisCacheManager implements ApplicationContextAware, InitializingBean {
    /** key serializer pair */
    static final RedisSerializationContext.SerializationPair<String> STRING_PAIR =
            RedisSerializationContext.SerializationPair.fromSerializer(STRING_SERIALIZER);
    /** value serializer pair */
    static final RedisSerializationContext.SerializationPair<Object> JACKSON_PAIR =
            RedisSerializationContext.SerializationPair.fromSerializer(JACKSON_SERIALIZER);

    @SuppressWarnings("unused")
    public static final RedisSerializationContext.SerializationPair<Object> FAST_JSON_PAIR =
            RedisSerializationContext.SerializationPair.fromSerializer(FAST_JSON_SERIALIZER);
    private ApplicationContext applicationContext;
    private final Map<String, RedisCacheConfiguration> initialCacheConfiguration = new LinkedHashMap<>();

    public MyRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    public Cache getCache(@Nonnull String name) {
        Cache cache = super.getCache(name);
        return new RedisCacheWrapper(cache);
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            final Class clazz = applicationContext.getType(beanName);
            add(clazz);
        }
        super.afterPropertiesSet();
    }

    @Nonnull
    @Override
    protected Collection<RedisCache> loadCaches() {
        List<RedisCache> caches = new LinkedList<>();
        for (Map.Entry<String, RedisCacheConfiguration> entry : initialCacheConfiguration.entrySet()) {
            caches.add(super.createRedisCache(entry.getKey(), entry.getValue()));
        }
        return caches;
    }

    private void add(final Class clazz) {
        ReflectionUtils.doWithMethods(
                clazz,
                method -> {
                    ReflectionUtils.makeAccessible(method);
                    CacheExpire cacheExpire = AnnotationUtils.findAnnotation(method, CacheExpire.class);
                    if (cacheExpire == null) {
                        return;
                    }
                    Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
                    if (cacheable != null) {
                        add(cacheable.cacheNames(), cacheExpire);
                        return;
                    }
                    Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
                    if (caching != null) {
                        Cacheable[] cs = caching.cacheable();
                        if (cs.length > 0) {
                            Arrays.stream(cs).filter(Objects::nonNull).forEach(c -> add(c.cacheNames(), cacheExpire));
                        }
                    } else {
                        CacheConfig cacheConfig = AnnotationUtils.findAnnotation(clazz, CacheConfig.class);
                        if (cacheConfig != null) {
                            add(cacheConfig.cacheNames(), cacheExpire);
                        }
                    }
                },
                method -> null != AnnotationUtils.findAnnotation(method, CacheExpire.class));
    }

    private void add(String[] cacheNames, CacheExpire cacheExpire) {
        Arrays.stream(cacheNames).filter(cacheName -> !StringUtils.isBlank(cacheName)).forEach(cacheName -> {
            long expire = cacheExpire.expire();
            log.info("cacheName: {}, expire: {}", cacheName, expire);
            if (expire >= 0) {
                // 缓存配置
                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(expire))
                        .disableCachingNullValues()
                        // .prefixKeysWith(cacheName)
                        .serializeKeysWith(STRING_PAIR)
                        .serializeValuesWith(JACKSON_PAIR);
                initialCacheConfiguration.put(cacheName, config);
            } else {
                log.warn("{} use default expiration.", cacheName);
            }
        });
    }

    protected static class RedisCacheWrapper implements Cache {
        private final Cache cache;

        RedisCacheWrapper(Cache cache) {
            this.cache = cache;
        }

        @Nonnull
        @Override
        public String getName() {
            // log.info("name: {}", cache.getName());
            try {
                return cache.getName();
            } catch (Exception e) {
                log.error("getName ---> err_msg: {}", e.getMessage(), e);
                //noinspection ConstantConditions
                return null;
            }
        }

        @Nonnull
        @Override
        public Object getNativeCache() {
            // log.info("nativeCache: {}", cache.getNativeCache());
            try {
                return cache.getNativeCache();
            } catch (Exception e) {
                log.error("getNativeCache ---> err_msg: {}", e.getMessage(), e);
                return null;
            }
        }

        @Override
        public ValueWrapper get(@Nonnull Object o) {
            // log.info("get ---> o: {}", o);
            try {
                return cache.get(o);
            } catch (Exception e) {
                log.error("get ---> o: {}, err_msg: {}", o, e.getMessage(), e);
                return null;
            }
        }

        @Override
        public <T> T get(@Nonnull Object o, Class<T> aClass) {
            // log.info("get ---> o: {}, clazz: {}", o, aClass);
            try {
                return cache.get(o, aClass);
            } catch (Exception e) {
                log.error("get ---> o: {}, clazz: {}, err_msg: {}", o, aClass, e.getMessage(), e);
                return null;
            }
        }

        @Override
        public <T> T get(@Nonnull Object o, @Nonnull Callable<T> callable) {
            // log.info("get ---> o: {}", o);
            try {
                return cache.get(o, callable);
            } catch (Exception e) {
                log.error("get ---> o: {}, err_msg: {}", o, e.getMessage(), e);
                return null;
            }
        }

        @Override
        public void put(@Nonnull Object o, Object o1) {
            // log.info("put ---> o: {}, o1: {}", o, o1);
            try {
                cache.put(o, o1);
            } catch (Exception e) {
                log.error("put ---> o: {}, o1: {}, err_msg: {}", o, o1, e.getMessage(), e);
            }
        }

        @Override
        public ValueWrapper putIfAbsent(@Nonnull Object o, Object o1) {
            // log.info("putIfAbsent ---> o: {}, o1: {}", o, o1);
            try {
                return cache.putIfAbsent(o, o1);
            } catch (Exception e) {
                log.error("putIfAbsent ---> o: {}, o1: {}, err_msg: {}", o, o1, e.getMessage(), e);
                return null;
            }
        }

        @Override
        public void evict(@Nonnull Object o) {
            // log.info("evict ---> o: {}", o);
            try {
                cache.evict(o);
            } catch (Exception e) {
                log.error("evict ---> o: {}, err_msg: {}", o, e.getMessage(), e);
            }
        }

        @Override
        public void clear() {
            // log.info("clear");
            try {
                cache.clear();
            } catch (Exception e) {
                log.error("clear ---> err_msg: {}", e.getMessage(), e);
            }
        }
    }
}
