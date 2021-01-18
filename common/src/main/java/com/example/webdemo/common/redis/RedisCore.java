package com.example.webdemo.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * spring boot redis工具类
 *
 * @author tangaq
 * @date 2020/12/1
 */
@Component
public class RedisCore {
    private static final Logger log = LoggerFactory.getLogger(RedisCore.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public Boolean expire(String key, long time) {
        Boolean flag = false;
        if (time > 0) {
            flag = redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        return flag;
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean exist(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("redis异常:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue()
            .get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue()
            .set(key, value);

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(毫秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue()
                .set(key, value, time, TimeUnit.MILLISECONDS);
        } else {
            set(key, value);
        }
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash()
            .get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash()
            .entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     */
    public void hmset(String key, Map<String, Object> map) {
        redisTemplate.opsForHash()
            .putAll(key, map);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     */
    public void hmset(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash()
            .putAll(key, map);
        if (time > 0) {
            expire(key, time);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key 键
     * @param item 项
     * @param value 值
     */
    public void hset(String key, String item, Object value) {
        redisTemplate.opsForHash()
            .put(key, item, value);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    public void hset(String key, String item, Object value, long time) {
        redisTemplate.opsForHash()
            .put(key, item, value);
        if (time > 0) {
            expire(key, time);
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash()
            .delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash()
            .hasKey(key, item);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        return redisTemplate.opsForSet()
            .members(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet()
            .isMember(key, value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSet(String key, Object... values) {
        return redisTemplate.opsForSet()
            .add(key, values);
    }

    /**
     * 将set数据放入缓存
     *
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSetAndTime(String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet()
            .add(key, values);
        if (time > 0) {
            expire(key, time);
        }
        return count;
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long sGetSetSize(String key) {
        return redisTemplate.opsForSet()
            .size(key);
    }

    /**
     * 移除值为value的
     *
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet()
            .remove(key, values);
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        return redisTemplate.opsForList()
            .range(key, start, end);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long lGetListSize(String key) {
        return redisTemplate.opsForList()
            .size(key);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        return redisTemplate.opsForList()
            .index(key, index);
    }

    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public Long lSet(String key, Object value) {
        return redisTemplate.opsForList()
            .rightPush(key, value);
    }

    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public Long lSet(String key, Object value, long time) {
        Long aLong = redisTemplate.opsForList()
            .rightPush(key, value);
        if (time > 0) {
            expire(key, time);
        }
        return aLong;
    }

    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public Long lSet(String key, List<Object> value) {
        return redisTemplate.opsForList()
            .rightPushAll(key, value);
    }

    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public Long lSet(String key, List<Object> value, long time) {
        Long aLong = redisTemplate.opsForList()
            .rightPushAll(key, value);
        if (time > 0) {
            expire(key, time);
        }
        return aLong;
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public void lUpdateIndex(String key, long index, Object value) {
        redisTemplate.opsForList()
            .set(key, index, value);
    }

    /**
     * 移除N个值为value
     *
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList()
            .remove(key, count, value);
    }

    /**
     * 默认600s
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setIfAbsent(String key, String value) {
        return setIfAbsent(key, value, 600);
    }

    /**
     * 线程安全设置缓存,默认:单位(秒)
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean setIfAbsent(String key, String value, long time) {
        boolean flag;
        if (time > 0) {
            try {
                // 如果能赋值则返回ture，如果有值返回false
                flag = redisTemplate.opsForValue()
                    .setIfAbsent(key, value, time, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("key[{}],设置超时时间失败,", key, e);
                return false;
            }
        } else {
            flag = redisTemplate.opsForValue()
                .setIfAbsent(key, value);
        }
        return flag;
    }

    // ============================Stringset=============================
    /**
     * 字符缓存放入
     *
     * @param key 键
     * @param value 值
     */
    public void setString(String key, String value) {
        stringRedisTemplate.opsForValue()
            .set(key, value);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(毫秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public void setString(String key, String value, long time) {
        if (time > 0) {
            stringRedisTemplate.opsForValue()
                .set(key, value, time, TimeUnit.MILLISECONDS);
        } else {
            setString(key, value);
        }
    }

    /**
     * 获取string缓存
     *
     * @param key 键
     */
    public String getString(String key) {
        return stringRedisTemplate.opsForValue()
            .get(key);
    }

    /**
     * 可用前提:setString(string,string)
     * 增加+i
     * @param key
     * @param i
     * @return 增加后的值
     */
    public Long increment(String key, long i) {
        return stringRedisTemplate.opsForValue()
            .increment(key, i);
    }

    /**
     * 可用前提:setString(string,string)
     * 减少+i
     * @param key
     * @param i
     * @return 减少后的值
     */
    public Long decrement(String key, long i) {
        return stringRedisTemplate.opsForValue()
            .decrement(key, i);
    }
}