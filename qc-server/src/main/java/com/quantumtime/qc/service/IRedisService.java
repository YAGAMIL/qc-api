package com.quantumtime.qc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * .Description:RedisService & Created on 2019/11/14 13:36
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "unchecked", "AlibabaLowerCamelCaseVariableNaming"})
public interface IRedisService<K, V> {

    /**
     * 用户排序通过注册时间的 权重值
     *
     * @param date the date
     * @return create time score
     */
    double getCreateTimeScore(long date);

    /**
     * 获取Redis中所有的键的key
     *
     * @return all keys
     */
    Set<K> getAllKeys();

    /**
     * 获取所有的普通key-value
     *
     * @return all string
     */
    Map<K, V> getAllString();

    /**
     * 获取所有的Set -key-value
     *
     * @return all set
     */
    Map<K, Set<V>> getAllSet();

    /**
     * 获取所有的ZSet正序 -key-value 不获取权重值
     *
     * @return all z set reverse range
     */
    Map<K, Set<V>> getAllZsetReverseRange();

    /**
     * 获取所有的ZSet倒序 -key-value 不获取权重值
     *
     * @return all z set range
     */
    Map<K, Set<V>> getAllZsetRange();

    /**
     * 获取所有的List -key-value
     *
     * @return all list
     */
    Map<K, List<V>> getAllList();

    /**
     * 获取所有的Map -key-value
     *
     * @return all map
     */
    Map<K, Map<K, V>> getAllMap();

    /**
     * 添加一个list
     *
     * @param key the key
     * @param objectList the object list
     */
    void addList(K key, List<V> objectList);

    /**
     * 向list中增加值
     *
     * @param key the key
     * @param obj the obj
     * @return 返回在list中的下标 long
     */
    long addList(K key, V obj);

    /**
     * 向list中增加值
     *
     * @param key the key
     * @param obj the obj
     * @return 返回在list中的下标 long
     */
    long addList(K key, V... obj);

    /**
     * 输出list
     *
     * @param key List的key
     * @param s 开始下标
     * @param e 结束的下标
     * @return list list
     */
    List<V> getList(K key, long s, long e);

    /**
     * 输出完整的list
     *
     * @param key the key
     * @return the list
     */
    List<V> getList(K key);

    /**
     * 获取list集合中元素的个数
     *
     * @param key the key
     * @return list size
     */
    long getListSize(K key);

    /**
     * 移除list中某值 移除list中 count个value为object的值,并且返回移除的数量, 如果count为0,或者大于list中为value为object数量的总和,
     * 那么移除所有value为object的值,并且返回移除数量
     *
     * @param key the key
     * @param object the object
     * @return 返回移除数量 long
     */
    long removeListValue(K key, V object);

    /**
     * 移除list中某值
     *
     * @param key the key
     * @param object the object
     * @return 返回移除数量 long
     */
    long removeListValue(K key, V... object);

    /**
     * 批量删除key对应的value
     *
     * @param keys the keys
     */
    void remove(final K... keys);

    /**
     * 删除缓存 根据key精确匹配删除
     *
     * @param key the key
     */
    void remove(final K key);

    /**
     * 通过分数删除ZSet中的值
     *
     * @param key the key
     * @param s the s
     * @param e the e
     */
    void removeZsetRangeByScore(String key, double s, double e);

    /**
     * 设置Set的过期时间
     *
     * @param key the key
     * @param time the time
     * @return set expire time
     */
    Boolean setSetExpireTime(String key, Long time);

    /**
     * 设置ZSet的过期时间
     *
     * @param key the key
     * @param time the time
     * @return z set expire time
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    Boolean setZsetExpireTime(String key, Long time);

    /**
     * 判断缓存中是否有key对应的value
     *
     * @param key the key
     * @return boolean boolean
     */
    boolean exists(final K key);

    /**
     * 读取String缓存 可以是对象
     *
     * @param key the key
     * @return v v
     */
    V get(final K key);

    /**
     * 读取String缓存 可以是对象
     *
     * @param key the key
     * @return list list
     */
    List<V> get(final K... key);

    /**
     * 读取缓存 可以是对象 根据正则表达式匹配
     *
     * @param regKey the reg key
     * @return by regular
     */
    List<Object> getByRegular(final K regKey);

    /**
     * 写入缓存 可以是对象
     *
     * @param key the key
     * @param value the value
     */
    void set(final K key, V value);

    /**
     * 写入缓存
     *
     * @param key the key
     * @param value the value
     * @param expireTime 过期时间 -单位s
     */
    void set(final K key, V value, Long expireTime);

    /**
     * 设置一个key的过期时间（单位：秒）
     *
     * @param key the key
     * @param expireTime the expire time
     * @return expire time
     */
    boolean setExpireTime(K key, Long expireTime);

    /**
     * 获取key的类型
     *
     * @param key the key
     * @return type type
     */
    DataType getType(K key);

    /**
     * 删除map中的某个对象
     *
     * @param key map对应的key
     * @param field map中该对象的key
     */
    void removeMapField(K key, V... field);

    /**
     * Gets map.
     *
     * @param key the key map对应的key
     * @return the map
     */
    Map<K, V> getMap(K key);

    /**
     * Gets map size.获取map对象
     *
     * @param key the key map对应的key
     * @return the map size
     */
    Long getMapSize(K key);

    /**
     * 获取map缓存中的某个对象
     *
     * @param <T> the type parameter
     * @param key map对应的key
     * @param field map中该对象的key
     * @return map field
     */
    <T> T getMapField(K key, K field);

    /**
     * 判断map中对应key的key是否存在
     *
     * @param key map对应的key
     * @param field the field
     * @return boolean boolean
     */
    Boolean hasMapKey(K key, K field);

    /**
     * 获取map对应key的value
     *
     * @param key map对应的key
     * @return map field value
     */
    List<V> getMapFieldValue(K key);

    /**
     * 获取map的key
     *
     * @param key map对应的key
     * @return map field key
     */
    Set<V> getMapFieldKey(K key);

    /**
     * 添加map
     *
     * @param key the key
     * @param map the map
     */
    void addMap(K key, Map<K, V> map);

    /**
     * 向key对应的map中添加缓存对象
     *
     * @param key cache对象key
     * @param field map对应的key
     * @param value 值
     */
    void addMap(K key, K field, Object value);

    /**
     * 向key对应的map中添加缓存对象
     *
     * @param key cache对象key
     * @param field map对应的key
     * @param value 值
     * @param time 过期时间-整个MAP的过期时间
     */
    void addMap(K key, K field, V value, long time);

    /**
     * 向set中加入对象
     *
     * @param key 对象key
     * @param obj 值
     */
    void addSet(K key, V... obj);

    /**
     * 处理事务时锁定key
     *
     * @param key the key
     */
    void watch(String key);

    /**
     * 移除set中的某些值
     *
     * @param key 对象key
     * @param obj 值
     * @return the long
     */
    long removeSetValue(K key, V obj);

    /**
     * 移除set中的某些值
     *
     * @param key 对象key
     * @param obj 值
     * @return the long
     */
    long removeSetValue(K key, V... obj);

    /**
     * 获取set的对象数
     *
     * @param key 对象key
     * @return the set size
     */
    long getSetSize(K key);

    /**
     * 判断set中是否存在这个值
     *
     * @param key 对象key
     * @param obj the obj
     * @return the boolean
     */
    Boolean hasSetValue(K key, V obj);

    /**
     * 获得整个set
     *
     * @param key 对象key
     * @return the set
     */
    Set<V> getSet(K key);

    /**
     * 获得set 并集
     *
     * @param key the key
     * @param otherKey the other key
     * @return set union
     */
    Set<V> getSetUnion(K key, K otherKey);

    /**
     * 获得set 并集
     *
     * @param key the key
     * @param set the set
     * @return set union
     */
    Set<V> getSetUnion(K key, Set<Object> set);

    /**
     * 获得set 交集
     *
     * @param key the key
     * @param otherKey the other key
     * @return set intersect
     */
    Set<V> getSetIntersect(K key, K otherKey);

    /**
     * 获得set 交集
     *
     * @param key the key
     * @param set the set
     * @return set intersect
     */
    Set<V> getSetIntersect(K key, Set<Object> set);

    /**
     * 模糊移除 支持*号等匹配移除
     *
     * @param blear the blear
     */
    void removeBlear(K... blear);

    /**
     * 修改key名 如果不存在该key或者没有修改成功返回false
     *
     * @param oldKey the old key
     * @param newKey the new key
     * @return boolean boolean
     */
    Boolean renameIfAbsent(String oldKey, String newKey);

    /**
     * 模糊移除 支持*号等匹配移除
     *
     * @param blear the blear
     */
    void removeBlear(K blear);

    /**
     * 根据正则表达式来移除key-value
     *
     * @param regular the regular
     */
    void removeByRegular(String... regular);

    /**
     * 根据正则表达式来移除key-value
     *
     * @param regular the regular
     */
    void removeByRegular(String regular);

    /**
     * 根据正则表达式来移除 Map中的key-value
     *
     * @param key the key
     * @param regular the 正则表达式
     */
    void removeMapFieldByRegular(K key, K... regular);

    /**
     * 根据正则表达式来移除 Map中的key-value
     *
     * @param key the key
     * @param blear the blear
     */
    void removeMapFieldByRegular(K key, K blear);

    /**
     * 移除key 对应的value
     *
     * @param key the key
     * @param value the value
     * @return long long
     */
    Long removeZsetValue(K key, V... value);

    /**
     * 移除key ZSet
     *
     * @param key the key
     */
    void removeZset(K key);

    /**
     * 删除，键为K的集合，索引start<=index<=end的元素子集
     *
     * @param key the key
     * @param start the start
     * @param end the end
     */
    void removeZsetRange(K key, Long start, Long end);

    /**
     * 并集 将key对应的集合和key1对应的集合合并到key2中 如果分数相同的值，都会保留 原来key2的值会被覆盖
     *
     * @param key the key
     * @param key1 the key 1
     * @param key2 the key 2
     */
    void setZsetUnionAndStore(String key, String key1, String key2);

    /**
     * 获取整个有序集合ZSET，正序
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the z set range
     */
    <T> T getZsetRange(K key);

    /**
     * 获取有序集合ZSET 键为K的集合，索引start<=index<=end的元素子集，正序
     *
     * @param <T> the type parameter
     * @param key the key
     * @param start 开始位置
     * @param end 结束位置
     * @return the z set range
     */
    <T> T getZsetRange(K key, long start, long end);

    /**
     * 获取整个有序集合ZSET，倒序
     *
     * @param key the key
     * @return the z set reverse range
     */
    Set<Object> getZsetReverseRange(K key);

    /**
     * 获取有序集合ZSET 键为K的集合，索引start<=index<=end的元素子集，倒序
     *
     * @param key the key
     * @param start 开始位置
     * @param end 结束位置
     * @return the z set reverse range
     */
    Set<V> getZsetReverseRange(K key, long start, long end);

    /**
     * 通过分数(权值)获取ZSET集合 正序 -从小到大
     *
     * @param key the key
     * @param start the start
     * @param end the end
     * @return z set range by score
     */
    Set<V> getZsetRangeByScore(String key, double start, double end);

    /**
     * 通过分数(权值)获取ZSET集合 倒序 -从大到小
     *
     * @param key the key
     * @param start the start
     * @param end the end
     * @return z set reverse range by score
     */
    Set<V> getZsetReverseRangeByScore(String key, double start, double end);

    /**
     * 键为K的集合，索引start<=index<=end的元素子集 返回泛型接口（包括score和value），正序
     *
     * @param key the key
     * @param start the start
     * @param end the end
     * @return z set range with scores
     */
    Set<ZSetOperations.TypedTuple<V>> getZsetRangeWithScores(K key, long start, long end);

    /**
     * 键为K的集合，索引start<=index<=end的元素子集 返回泛型接口（包括score和value），倒序
     *
     * @param key the key
     * @param start the start
     * @param end the end
     * @return z set reverse range with scores
     */
    Set<ZSetOperations.TypedTuple<V>> getZsetReverseRangeWithScores(K key, long start, long end);

    /**
     * 键为K的集合 返回泛型接口（包括score和value），正序
     *
     * @param key the key
     * @return z set range with scores
     */
    Set<ZSetOperations.TypedTuple<V>> getZsetRangeWithScores(K key);

    /**
     * 键为K的集合 返回泛型接口（包括score和value），倒序
     *
     * @param key the key
     * @return z set reverse range with scores
     */
    Set<ZSetOperations.TypedTuple<V>> getZsetReverseRangeWithScores(K key);

    /**
     * 键为K的集合，sMin<=score<=sMax的元素个数
     *
     * @param key the key
     * @param sMin the s min
     * @param sMax the s max
     * @return z set count size
     */
    long getZsetCountSize(K key, double sMin, double sMax);

    /**
     * 获取Zset 键为K的集合元素个数
     *
     * @param key the key
     * @return z set size
     */
    long getZsetSize(K key);

    /**
     * 获取键为K的集合，value为obj的元素分数
     *
     * @param key the key
     * @param value the value
     * @return z set score
     */
    double getZsetScore(K key, V value);

    /**
     * 元素分数增加，delta是增量
     *
     * @param key the key
     * @param value the value
     * @param delta the delta
     * @return double double
     */
    double incrementZsetScore(K key, V value, double delta);

    /**
     * 添加有序集合ZSET 默认按照score升序排列，存储格式K(1)==V(n)，V(1)=S(1)
     *
     * @param key the key
     * @param score the score
     * @param value the value
     * @return boolean boolean
     */
    Boolean addZset(String key, double score, Object value);

    /**
     * 添加有序集合ZSET
     *
     * @param key the key
     * @param value the value
     * @return long long
     */
    Long addZset(K key, TreeSet<V> value);

    /**
     * 添加有序集合ZSET
     *
     * @param key the key
     * @param score the score
     * @param value the value
     * @return boolean boolean
     */
    Boolean addZset(K key, double[] score, Object[] value);
}
