package com.quantumtime.qc.service.impl;

import com.quantumtime.qc.service.IRedisService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/** The type Redis service. */
@Slf4j
@Service
public class RedisServiceImpl implements IRedisService<String, Object> {

    /** 出异常，重复操作的次数 */
    @SuppressWarnings("FieldCanBeLocal")
    private static Integer times = 3;

    /** The Redis template. */
    @Resource protected RedisTemplate redisTemplate;

    @Override
    public double getCreateTimeScore(long date) {
        return date / 100000.0;
    }

    @Override
    public Set<String> getAllKeys() {
        return redisTemplate.keys("*");
    }

    @Override
    public Map<String, Object> getAllString() {
        Set<String> stringSet = getAllKeys();
        return stringSet.stream().filter(k -> getType(k) == DataType.STRING).collect(Collectors.toMap(
                k -> k, this::get, (a, b) -> b));
    }

    @Override
    public Map<String, Set<Object>> getAllSet() {
        Set<String> stringSet = getAllKeys();
        return stringSet.stream().filter(k -> getType(k) == DataType.SET).collect(Collectors.toMap(
                k -> k, this::getSet, (a, b) -> b));
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Override
    public Map<String, Set<Object>> getAllZsetRange() {
        Set<String> stringSet = getAllKeys();
        return stringSet.stream().filter(k -> getType(k) == DataType.ZSET).collect(Collectors.toMap(
                k -> k, this::getZsetRange, (a, b) -> b));
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Override
    public Map<String, Set<Object>> getAllZsetReverseRange() {
        Set<String> stringSet = getAllKeys();
        return stringSet.stream().filter(k -> getType(k) == DataType.ZSET).collect(Collectors.toMap(
                k -> k, this::getZsetReverseRange, (a, b) -> b));
    }

    @Override
    public Map<String, List<Object>> getAllList() {
        Set<String> stringSet = getAllKeys();
        return stringSet.stream().filter(k -> getType(k) == DataType.LIST).collect(Collectors.toMap(
                k -> k, this::getList, (a, b) -> b));
    }

    @Override
    public Map<String, Map<String, Object>> getAllMap() {
        Set<String> stringSet = getAllKeys();
        return stringSet.stream().filter(k -> getType(k) == DataType.HASH).collect(Collectors.toMap(
                k -> k, this::getMap, (a, b) -> b));
    }

    @Override
    public void addList(String key, List<Object> objectList) {
        objectList.forEach(obj -> addList(key, obj));
    }

    @Override
    public long addList(String key, Object obj) {
        return redisTemplate.boundListOps(key).rightPush(obj);
    }

    @Override
    public long addList(String key, Object... obj) {
        return redisTemplate.boundListOps(key).rightPushAll(obj);
    }

    @Override
    public List<Object> getList(String key, long s, long e) {
        return redisTemplate.boundListOps(key).range(s, e);
    }

    @Override
    public List<Object> getList(String key) {
        return redisTemplate.boundListOps(key).range(0, getListSize(key));
    }

    @Override
    public long getListSize(String key) {
        return redisTemplate.boundListOps(key).size();
    }

    @Override
    public long removeListValue(String key, Object object) {
        return redisTemplate.boundListOps(key).remove(0, object);
    }

    @Override
    public long removeListValue(String key, Object... objects) {
        return Arrays.stream(objects).mapToLong(object -> removeListValue(key, object)).sum();
    }

    @Override
    public void remove(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                remove(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    @Override
    public void removeBlear(String... blears) {
        Arrays.stream(blears).forEach(this::removeBlear);
    }

    @Override
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    @Override
    public void removeBlear(String blear) {
        redisTemplate.delete(redisTemplate.keys(blear));
    }

    @Override
    public void removeByRegular(String... blears) {
        Arrays.stream(blears).forEach(this::removeBlear);
    }

    @Override
    public void removeByRegular(String blear) {
        Set<String> stringSet = getAllKeys();
        stringSet.stream().filter(s -> Pattern.compile(blear).matcher(s).matches()).forEach(s ->
                redisTemplate.delete(s));
    }

    @Override
    public void removeMapFieldByRegular(String key, String... regular) {
        Arrays.stream(regular).forEach(blear -> removeMapFieldByRegular(key, blear));
    }

    @Override
    public void removeMapFieldByRegular(String key, String blear) {
        Map<String, Object> map = getMap(key);
        Set<String> stringSet = map.keySet();
        stringSet.stream().filter(s -> Pattern.compile(blear).matcher(s).matches()).forEach(s ->
                redisTemplate.boundHashOps(key).delete(s));
    }

    @Override
    public Long removeZsetValue(String key, Object... value) {
        return redisTemplate.boundZSetOps(key).remove(value);
    }

    @Override
    public void removeZset(String key) {
        removeZsetRange(key, 0L, getZsetSize(key));
    }

    @Override
    public void removeZsetRange(String key, Long start, Long end) {
        redisTemplate.boundZSetOps(key).removeRange(start, end);
    }

    @Override
    public void setZsetUnionAndStore(String key, String key1, String key2) {
        redisTemplate.boundZSetOps(key).unionAndStore(key1, key2);
    }

    @Override
    public Set<Object> getZsetRange(String key) {
        return getZsetRange(key, 0, getZsetSize(key));
    }

    @Override
    public Set<Object> getZsetRange(String key, long s, long e) {
        return redisTemplate.boundZSetOps(key).range(s, e);
    }

    @Override
    public Set<Object> getZsetReverseRange(String key) {
        return getZsetReverseRange(key, 0, getZsetSize(key));
    }

    @Override
    public Set<Object> getZsetReverseRange(String key, long start, long end) {
        return redisTemplate.boundZSetOps(key).reverseRange(start, end);
    }

    @Override
    public Set<Object> getZsetRangeByScore(String key, double start, double end) {
        return redisTemplate.boundZSetOps(key).rangeByScore(start, end);
    }

    @Override
    public Set<Object> getZsetReverseRangeByScore(String key, double start, double end) {
        return redisTemplate.boundZSetOps(key).reverseRangeByScore(start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> getZsetRangeWithScores(String key, long start, long end) {
        return redisTemplate.boundZSetOps(key).rangeWithScores(start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> getZsetReverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.boundZSetOps(key).reverseRangeWithScores(start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> getZsetRangeWithScores(String key) {
        return getZsetRangeWithScores(key, 0, getZsetSize(key));
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> getZsetReverseRangeWithScores(String key) {
        return getZsetReverseRangeWithScores(key, 0, getZsetSize(key));
    }

    @Override
    public long getZsetCountSize(String key, double sMin, double sMax) {
        return redisTemplate.boundZSetOps(key).count(sMin, sMax);
    }

    @Override
    public long getZsetSize(String key) {
        return redisTemplate.boundZSetOps(key).size();
    }

    @Override
    public double getZsetScore(String key, Object value) {
        return redisTemplate.boundZSetOps(key).score(value);
    }

    @Override
    public double incrementZsetScore(String key, Object value, double delta) {
        return redisTemplate.boundZSetOps(key).incrementScore(value, delta);
    }

    @Override
    public Boolean addZset(String key, double score, Object value) {
        return redisTemplate.boundZSetOps(key).add(value, score);
    }

    @Override
    public Long addZset(String key, TreeSet<Object> value) {
        return redisTemplate.boundZSetOps(key).add(value);
    }

    @Override
    public Boolean addZset(String key, double[] score, Object[] value) {
        return score.length == value.length
                && IntStream.range(0, score.length).allMatch(i -> addZset(key, score[i], value[i]));
    }

    @Override
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public void removeZsetRangeByScore(String key, double s, double e) {
        redisTemplate.boundZSetOps(key).removeRangeByScore(s, e);
    }

    @Override
    public Boolean setSetExpireTime(String key, Long time) {
        return redisTemplate.boundSetOps(key).expire(time, TimeUnit.SECONDS);
    }

    @Override
    public Boolean setZsetExpireTime(String key, Long time) {
        return redisTemplate.boundZSetOps(key).expire(time, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public Object get(int key) {
        return this.get(String.valueOf(key));
    }

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public Object get(long key) {
        return this.get(String.valueOf(key));
    }

    @Override
    public Object get(String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    @Override
    public List<Object> get(String... keys) {
        return Arrays.stream(keys).map(this::get).collect(Collectors.toList());
    }

    @Override
    public List<Object> getByRegular(String regKey) {
        return getAllKeys().stream()
                .filter(s -> Pattern.compile(regKey).matcher(s).matches() && getType(s) == DataType.STRING)
                .map(this::get)
                .collect(Collectors.toList());
    }

    /**
     * Set.
     *
     * @param key the key
     * @param value the value
     */
    public void set(long key, Object value) {
        this.set(String.valueOf(key), value);
    }

    /**
     * Set.
     *
     * @param key the key
     * @param value the value
     */
    public void set(int key, Object value) {
        this.set(String.valueOf(key), value);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.boundValueOps(key).set(value);
    }

    @Override
    public void set(String key, Object value, Long expireTime) {
        redisTemplate.boundValueOps(key).set(value, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public boolean setExpireTime(String key, Long expireTime) {
        return redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public DataType getType(String key) {
        return redisTemplate.type(key);
    }

    @Override
    public void removeMapField(String key, Object... field) {
        redisTemplate.boundHashOps(key).delete(field);
    }

    @Override
    public Long getMapSize(String key) {
        return redisTemplate.boundHashOps(key).size();
    }

    @Override
    public Map<String, Object> getMap(String key) {
        return redisTemplate.boundHashOps(key).entries();
    }

    @Override
    public <T> T getMapField(String key, String field) {
        return (T) redisTemplate.boundHashOps(key).get(field);
    }

    @Override
    public Boolean hasMapKey(String key, String field) {
        return redisTemplate.boundHashOps(key).hasKey(field);
    }

    @Override
    public List<Object> getMapFieldValue(String key) {
        return redisTemplate.boundHashOps(key).values();
    }

    @Override
    public Set<Object> getMapFieldKey(String key) {
        return redisTemplate.boundHashOps(key).keys();
    }

    @Override
    public void addMap(String key, Map<String, Object> map) {
        redisTemplate.boundHashOps(key).putAll(map);
    }

    @Override
    public void addMap(String key, String field, Object value) {
        redisTemplate.boundHashOps(key).put(field, value);
    }

    @Override
    public void addMap(String key, String field, Object value, long time) {
        redisTemplate.boundHashOps(key).put(field, value);
        redisTemplate.boundHashOps(key).expire(time, TimeUnit.SECONDS);
    }

    @Override
    public void watch(String key) {
        redisTemplate.watch(key);
    }

    @Override
    public void addSet(String key, Object... obj) {
        redisTemplate.boundSetOps(key).add(obj);
    }

    @Override
    public long removeSetValue(String key, Object obj) {
        return redisTemplate.boundSetOps(key).remove(obj);
    }

    @Override
    public long removeSetValue(String key, Object... obj) {
        return obj != null && obj.length > 0 ? redisTemplate.boundSetOps(key).remove(obj) : Long.valueOf(0L);
    }

    @Override
    public long getSetSize(String key) {
        return redisTemplate.boundSetOps(key).size();
    }

    @Override
    public Boolean hasSetValue(String key, Object obj) {
        Boolean boo = null;
        int t = 0;
        while (true) {
            try {
                boo = redisTemplate.boundSetOps(key).isMember(obj);
                break;
            } catch (Exception e) {
                log.error("key[" + key + "],obj[" + obj + "]判断Set中的值是否存在失败,异常信息:" + e.getMessage());
                t++;
            }
            if (t > times) {
                break;
            }
        }
        log.info("key[" + key + "],obj[" + obj + "]是否存在,boo:" + boo);
        return boo;
    }

    @Override
    public Set<Object> getSet(String key) {
        return redisTemplate.boundSetOps(key).members();
    }

    @Override
    public Set<Object> getSetUnion(String key, String otherKey) {
        return redisTemplate.boundSetOps(key).union(otherKey);
    }

    @Override
    public Set<Object> getSetUnion(String key, Set<Object> set) {
        return redisTemplate.boundSetOps(key).union(set);
    }

    @Override
    public Set<Object> getSetIntersect(String key, String otherKey) {
        return redisTemplate.boundSetOps(key).intersect(otherKey);
    }

    @Override
    public Set<Object> getSetIntersect(String key, Set<Object> set) {
        return redisTemplate.boundSetOps(key).intersect(set);
    }
}
