package com.fengx.template.utils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * 封装常用方法
 */
@Component
@RequiredArgsConstructor
public class RedisUtils {


    public final String REDIS_KEY_PERMISSION_LIST = "redis_key_permission_list";         // url权限列表
    public final String REDIS_KEY_FILTER_REJECT_LIST = "redis_key_filter_reject_list";   // 放行url列表

    private final int REDIS_KEY_EXPIRATION_TIME = 60 * 60 * 24;                          // 默认过期时间一天 单位秒
    private final @NonNull StringRedisTemplate redisTemplate;

    // -------------------key相关操作---------------------

    /**
     * 删除key
     *
     * @param key key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 清空缓存
     * 主要用在开发中
     */
    public void deleteAll() {
        Set<String> set = redisTemplate.keys("*");
        if (set != null && set.size() != 0) {
            redisTemplate.delete(set);
        }
    }

    /**
     * 是否存在key
     *
     * @param key key
     * @return Boolean
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key key
     * @param timeout 过期时间
     * @param unit 时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
     *             秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key key
     * @param date date 过期日期
     */
    public void expire(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    // -------------------对象相关操作--------------------- */

    /**
     * 设置指定 key 的值
     * @param key key
     * @param value value
     */
    public void set(String key, String value) {
        set(key, value, REDIS_KEY_EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    /**
     * 获取指定 key 的值
     * @param key key
     * @return Object
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取
     *
     * @param keys keys
     * @return list
     */
    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
     *
     * @param key key
     * @param value value
     * @param timeout
     *            过期时间
     * @param unit
     *            时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
     *            秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 批量添加
     * @param maps maps
     */
    public void multiSet(Map<String, String> maps) {
        redisTemplate.opsForValue().multiSet(maps);
    }

    // ------------------------list相关操作---------------------------- */

    /**
     * 获取list缓存的内容
     * @param key 键
     */
    public List<String> lGet(String key){
        // 0 到 -1代表所有值
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     */
    public void lSet(String key, List<String> value) {
        delete(key);
        lRightPushAll(key, value);
        expire(key, REDIS_KEY_EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     */
    public void lSet(String key, List<String> value, long time) {
        delete(key);
        lRightPushAll(key, value);
        if (time > 0) {
            expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 通过索引获取列表中的元素
     *
     * @param key key
     * @param index 索引
     * @return o
     */
    public String lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 从元素前面添加元素
     *
     * @param key key
     * @param value value
     * @return Long
     */
    public Long lLeftPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从元素前面添加元素
     * @param key key
     * @param value value
     * @return  Long
     */
    public Long lLeftPushAll(String key, String... value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 从元素前面添加元素
     * @param key key
     * @param value values
     * @return Long
     */
    public Long lLeftPushAll(String key, Collection<String> value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 当list存在的时候才加入
     *
     * @param key key
     * @param value value
     * @return Long
     */
    public Long lLeftPushIfPresent(String key, String value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 如果pivot存在,再pivot前面添加
     *
     * @param key key
     * @param pivot 元素
     * @param value value
     * @return Long
     */
    public Long lLeftPush(String key, String pivot, String value) {
        return redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     *从最后一个元素后面添加元素
     * @param key key
     * @param value value
     * @return Long
     */
    public Long lRightPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从最后一个元素后面添加多个元素
     * @param key key
     * @param value values
     */
    public void lRightPushAll(String key, String... value) {
        redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 从最后一个元素后面添加多个元素
     * @param key key
     * @param values values
     */
    public void lRightPushAll(String key, Collection<String> values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 为已存在的列表添加值
     *
     * @param key key
     * @param value value
     * @return Long
     */
    public Long lRightPushIfPresent(String key, String value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 在pivot元素的右边添加值
     *
     * @param key key
     * @param pivot pivot
     * @param value value
     * @return Long
     */
    public Long lRightPush(String key, String pivot, String value) {
        return redisTemplate.opsForList().rightPush(key, pivot, value);
    }

    /**
     * 移出并获取列表的第一个元素
     *
     * @param key key
     * @return 删除的元素
     */
    public String lLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key key
     * @param timeout
     *            等待时间
     * @param unit
     *            时间单位
     * @return Object
     */
    public String lBLeftPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除并获取列表最后一个元素
     *
     * @param key key
     * @return 删除的元素
     */
    public Object lRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key key
     * @param timeout
     *            等待时间
     * @param unit
     *            时间单位
     * @return o
     */
    public Object lBRightPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 删除集合中值等于value得元素
     *
     * @param key key
     * @param index
     *            index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
     *            index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value value
     * @return Long
     */
    public Long lRemove(String key, long index, Object value) {
        return redisTemplate.opsForList().remove(key, index, value);
    }

    /**
     * 获取列表长度
     *
     * @param key key
     * @return Long
     */
    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    // --------------------set相关操作-------------------------- */
//
//    /**
//     * set添加元素
//     *
//     * @param key
//     * @param values
//     * @return
//     */
//    public Long sAdd(String key, String... values) {
//        return redisTemplate.opsForSet().add(key, values);
//    }
//
//    /**
//     * set移除元素
//     *
//     * @param key
//     * @param values
//     * @return
//     */
//    public Long sRemove(String key, Object... values) {
//        return redisTemplate.opsForSet().remove(key, values);
//    }
//
//    /**
//     * 移除并返回集合的一个随机元素
//     *
//     * @param key
//     * @return
//     */
//    public String sPop(String key) {
//        return redisTemplate.opsForSet().pop(key);
//    }
//
//    /**
//     * 将元素value从一个集合移到另一个集合
//     *
//     * @param key
//     * @param value
//     * @param destKey
//     * @return
//     */
//    public Boolean sMove(String key, String value, String destKey) {
//        return redisTemplate.opsForSet().move(key, value, destKey);
//    }
//
//    /**
//     * 获取集合的大小
//     *
//     * @param key
//     * @return
//     */
//    public Long sSize(String key) {
//        return redisTemplate.opsForSet().size(key);
//    }
//
//    /**
//     * 判断集合是否包含value
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public Boolean sIsMember(String key, Object value) {
//        return redisTemplate.opsForSet().isMember(key, value);
//    }
//
//    /**
//     * 获取两个集合的交集
//     *
//     * @param key
//     * @param otherKey
//     * @return
//     */
//    public Set<String> sIntersect(String key, String otherKey) {
//        return redisTemplate.opsForSet().intersect(key, otherKey);
//    }
//
//    /**
//     * 获取key集合与多个集合的交集
//     *
//     * @param key
//     * @param otherKeys
//     * @return
//     */
//    public Set<String> sIntersect(String key, Collection<String> otherKeys) {
//        return redisTemplate.opsForSet().intersect(key, otherKeys);
//    }
//
//    /**
//     * key集合与otherKey集合的交集存储到destKey集合中
//     *
//     * @param key
//     * @param otherKey
//     * @param destKey
//     * @return
//     */
//    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
//        return redisTemplate.opsForSet().intersectAndStore(key, otherKey,
//                destKey);
//    }
//
//    /**
//     * key集合与多个集合的交集存储到destKey集合中
//     *
//     * @param key
//     * @param otherKeys
//     * @param destKey
//     * @return
//     */
//    public Long sIntersectAndStore(String key, Collection<String> otherKeys,
//            String destKey) {
//        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys,
//                destKey);
//    }
//
//    /**
//     * 获取两个集合的并集
//     *
//     * @param key
//     * @param otherKeys
//     * @return
//     */
//    public Set<String> sUnion(String key, String otherKeys) {
//        return redisTemplate.opsForSet().union(key, otherKeys);
//    }
//
//    /**
//     * 获取key集合与多个集合的并集
//     *
//     * @param key
//     * @param otherKeys
//     * @return
//     */
//    public Set<String> sUnion(String key, Collection<String> otherKeys) {
//        return redisTemplate.opsForSet().union(key, otherKeys);
//    }
//
//    /**
//     * key集合与otherKey集合的并集存储到destKey中
//     *
//     * @param key
//     * @param otherKey
//     * @param destKey
//     * @return
//     */
//    public Long sUnionAndStore(String key, String otherKey, String destKey) {
//        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
//    }
//
//    /**
//     * key集合与多个集合的并集存储到destKey中
//     *
//     * @param key
//     * @param otherKeys
//     * @param destKey
//     * @return
//     */
//    public Long sUnionAndStore(String key, Collection<String> otherKeys,
//            String destKey) {
//        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
//    }
//
//    /**
//     * 获取两个集合的差集
//     *
//     * @param key
//     * @param otherKey
//     * @return
//     */
//    public Set<String> sDifference(String key, String otherKey) {
//        return redisTemplate.opsForSet().difference(key, otherKey);
//    }
//
//    /**
//     * 获取key集合与多个集合的差集
//     *
//     * @param key
//     * @param otherKeys
//     * @return
//     */
//    public Set<String> sDifference(String key, Collection<String> otherKeys) {
//        return redisTemplate.opsForSet().difference(key, otherKeys);
//    }
//
//    /**
//     * key集合与otherKey集合的差集存储到destKey中
//     *
//     * @param key
//     * @param otherKey
//     * @param destKey
//     * @return
//     */
//    public Long sDifference(String key, String otherKey, String destKey) {
//        return redisTemplate.opsForSet().differenceAndStore(key, otherKey,
//                destKey);
//    }
//
//    /**
//     * key集合与多个集合的差集存储到destKey中
//     *
//     * @param key
//     * @param otherKeys
//     * @param destKey
//     * @return
//     */
//    public Long sDifference(String key, Collection<String> otherKeys,
//            String destKey) {
//        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys,
//                destKey);
//    }
//
//    /**
//     * 获取集合所有元素
//     *
//     * @param key
//     * @return
//     */
//    public Set<String> setMembers(String key) {
//        return redisTemplate.opsForSet().members(key);
//    }
//
//    /**
//     * 随机获取集合中的一个元素
//     *
//     * @param key
//     * @return
//     */
//    public String sRandomMember(String key) {
//        return redisTemplate.opsForSet().randomMember(key);
//    }
//
//    /**
//     * 随机获取集合中count个元素
//     *
//     * @param key
//     * @param count
//     * @return
//     */
//    public List<String> sRandomMembers(String key, long count) {
//        return redisTemplate.opsForSet().randomMembers(key, count);
//    }
//
//    /**
//     * 随机获取集合中count个元素并且去除重复的
//     *
//     * @param key
//     * @param count
//     * @return
//     */
//    public Set<String> sDistinctRandomMembers(String key, long count) {
//        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
//    }
//
//    /**
//     *
//     * @param key
//     * @param options
//     * @return
//     */
//    public Cursor<String> sScan(String key, ScanOptions options) {
//        return redisTemplate.opsForSet().scan(key, options);
//    }
//
//    /**------------------zSet相关操作--------------------------------*/
//
//    /**
//     * 添加元素,有序集合是按照元素的score值由小到大排列
//     *
//     * @param key
//     * @param value
//     * @param score
//     * @return
//     */
//    public Boolean zAdd(String key, String value, double score) {
//        return redisTemplate.opsForZSet().add(key, value, score);
//    }
//
//    /**
//     *
//     * @param key
//     * @param values
//     * @return
//     */
//    public Long zAdd(String key, Set<TypedTuple<String>> values) {
//        return redisTemplate.opsForZSet().add(key, values);
//    }
//
//    /**
//     *
//     * @param key
//     * @param values
//     * @return
//     */
//    public Long zRemove(String key, Object... values) {
//        return redisTemplate.opsForZSet().remove(key, values);
//    }
//
//    /**
//     * 增加元素的score值，并返回增加后的值
//     *
//     * @param key
//     * @param value
//     * @param delta
//     * @return
//     */
//    public Double zIncrementScore(String key, String value, double delta) {
//        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
//    }
//
//    /**
//     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
//     *
//     * @param key
//     * @param value
//     * @return 0表示第一位
//     */
//    public Long zRank(String key, Object value) {
//        return redisTemplate.opsForZSet().rank(key, value);
//    }
//
//    /**
//     * 返回元素在集合的排名,按元素的score值由大到小排列
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public Long zReverseRank(String key, Object value) {
//        return redisTemplate.opsForZSet().reverseRank(key, value);
//    }
//
//    /**
//     * 获取集合的元素, 从小到大排序
//     *
//     * @param key
//     * @param start
//     *            开始位置
//     * @param end
//     *            结束位置, -1查询所有
//     * @return
//     */
//    public Set<String> zRange(String key, long start, long end) {
//        return redisTemplate.opsForZSet().range(key, start, end);
//    }
//
//    /**
//     * 获取集合元素, 并且把score值也获取
//     *
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     */
//    public Set<TypedTuple<String>> zRangeWithScores(String key, long start,
//            long end) {
//        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
//    }
//
//    /**
//     * 根据Score值查询集合元素
//     *
//     * @param key
//     * @param min
//     *            最小值
//     * @param max
//     *            最大值
//     * @return
//     */
//    public Set<String> zRangeByScore(String key, double min, double max) {
//        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
//    }
//
//    /**
//     * 根据Score值查询集合元素, 从小到大排序
//     *
//     * @param key
//     * @param min
//     *            最小值
//     * @param max
//     *            最大值
//     * @return
//     */
//    public Set<TypedTuple<String>> zRangeByScoreWithScores(String key,
//            double min, double max) {
//        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
//    }
//
//    /**
//     *
//     * @param key
//     * @param min
//     * @param max
//     * @param start
//     * @param end
//     * @return
//     */
//    public Set<TypedTuple<String>> zRangeByScoreWithScores(String key,
//            double min, double max, long start, long end) {
//        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max,
//                start, end);
//    }
//
//    /**
//     * 获取集合的元素, 从大到小排序
//     *
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     */
//    public Set<String> zReverseRange(String key, long start, long end) {
//        return redisTemplate.opsForZSet().reverseRange(key, start, end);
//    }
//
//    /**
//     * 获取集合的元素, 从大到小排序, 并返回score值
//     *
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     */
//    public Set<TypedTuple<String>> zReverseRangeWithScores(String key,
//            long start, long end) {
//        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start,
//                end);
//    }
//
//    /**
//     * 根据Score值查询集合元素, 从大到小排序
//     *
//     * @param key
//     * @param min
//     * @param max
//     * @return
//     */
//    public Set<String> zReverseRangeByScore(String key, double min,
//            double max) {
//        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
//    }
//
//    /**
//     * 根据Score值查询集合元素, 从大到小排序
//     *
//     * @param key
//     * @param min
//     * @param max
//     * @return
//     */
//    public Set<TypedTuple<String>> zReverseRangeByScoreWithScores(
//            String key, double min, double max) {
//        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,
//                min, max);
//    }
//
//    /**
//     *
//     * @param key
//     * @param min
//     * @param max
//     * @param start
//     * @param end
//     * @return
//     */
//    public Set<String> zReverseRangeByScore(String key, double min,
//            double max, long start, long end) {
//        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max,
//                start, end);
//    }
//
//    /**
//     * 根据score值获取集合元素数量
//     *
//     * @param key
//     * @param min
//     * @param max
//     * @return
//     */
//    public Long zCount(String key, double min, double max) {
//        return redisTemplate.opsForZSet().count(key, min, max);
//    }
//
//    /**
//     * 获取集合大小
//     *
//     * @param key
//     * @return
//     */
//    public Long zSize(String key) {
//        return redisTemplate.opsForZSet().size(key);
//    }
//
//    /**
//     * 获取集合大小
//     *
//     * @param key
//     * @return
//     */
//    public Long zZCard(String key) {
//        return redisTemplate.opsForZSet().zCard(key);
//    }
//
//    /**
//     * 获取集合中value元素的score值
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public Double zScore(String key, Object value) {
//        return redisTemplate.opsForZSet().score(key, value);
//    }
//
//    /**
//     * 移除指定索引位置的成员
//     *
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     */
//    public Long zRemoveRange(String key, long start, long end) {
//        return redisTemplate.opsForZSet().removeRange(key, start, end);
//    }
//
//    /**
//     * 根据指定的score值的范围来移除成员
//     *
//     * @param key
//     * @param min
//     * @param max
//     * @return
//     */
//    public Long zRemoveRangeByScore(String key, double min, double max) {
//        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
//    }
//
//    /**
//     * 获取key和otherKey的并集并存储在destKey中
//     *
//     * @param key
//     * @param otherKey
//     * @param destKey
//     * @return
//     */
//    public Long zUnionAndStore(String key, String otherKey, String destKey) {
//        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
//    }
//
//    /**
//     *
//     * @param key
//     * @param otherKeys
//     * @param destKey
//     * @return
//     */
//    public Long zUnionAndStore(String key, Collection<String> otherKeys,
//            String destKey) {
//        return redisTemplate.opsForZSet()
//                .unionAndStore(key, otherKeys, destKey);
//    }
//
//    /**
//     * 交集
//     *
//     * @param key
//     * @param otherKey
//     * @param destKey
//     * @return
//     */
//    public Long zIntersectAndStore(String key, String otherKey,
//            String destKey) {
//        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey,
//                destKey);
//    }
//
//    /**
//     * 交集
//     *
//     * @param key
//     * @param otherKeys
//     * @param destKey
//     * @return
//     */
//    public Long zIntersectAndStore(String key, Collection<String> otherKeys,
//            String destKey) {
//        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys,
//                destKey);
//    }
//
//    /**
//     *
//     * @param key
//     * @param options
//     * @return
//     */
//    public Cursor<TypedTuple<String>> zScan(String key, ScanOptions options) {
//        return redisTemplate.opsForZSet().scan(key, options);
//    }
}