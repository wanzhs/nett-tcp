package com.example.tcp.demo.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @author:wanzhongsu
 * @description: Redis工具类
 * @date:2019/8/12 11:03
 */
@Slf4j
public class MRedisUtil {

    @Resource
    protected RedisTemplate redisTemplate;

    /**
     * @author:wanzhongsu
     * @description: 添加
     * @date:2019/8/12 11:07
     */
    public <T> void put(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @author:wanzhongsu
     * @description: 添加—含有超时时间
     * @date:2019/8/12 11:07
     */
    public <T> void put(String key, T value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * @author:wanzhongsu
     * @description: 添加hash
     * @date:2019/8/12 11:07
     */
    public <T> void putHash(String key, String hashKey, T hashValue) {
        redisTemplate.opsForHash().put(key, hashKey, hashValue);
    }

    public <T> void putHash(String key, String hashKey, T hashValue, long time, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, hashKey, hashValue);
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * @author:wanzhongsu
     * @description: 是否hash
     * @date:2019/8/12 11:07
     */
    public boolean hasHash(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * @author:wanzhongsu
     * @description: 根据hashkey 获取
     * @date:2019/8/12 11:07
     */
    public <T> T getHash(String key, String hashKey) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * @author:wanzhongsu
     * @description: 添加hash 集合
     * @date:2019/8/12 11:06
     */
    public <K, T> void pushHashAll(String key, Map<K, T> hashValue) {
        redisTemplate.opsForHash().putAll(key, hashValue);
    }

    /**
     * @author:wanzhongsu
     * @description: 添加hash 集合 带有超时时间
     * @date:2019/8/12 11:06
     */
    public <K, T> void pushHashAll(String key, Map<K, T> hashValue, long time, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, hashValue);
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * @author:wanzhongsu
     * @description: 添加—含有超时时间
     * @date:2019/8/12 11:06
     */
    public <T> void put(String key, T value, long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    /**
     * @author:wanzhongsu
     * @description: 删除
     * @date:2019/8/12 11:06
     */
    public boolean remove(String key) {
        try {
            redisTemplate.opsForValue().getOperations().delete(key);
            return true;
        } catch (Throwable t) {
            log.error("redis remove " + key + " has error,detail----" + t);
        }
        return false;
    }

    /**
     * @author:wanzhongsu
     * @description: 删除hash 根据key
     * @date:2019/8/12 11:06
     */
    public <T> boolean removeHash(String key, T... hashKeys) {
        try {
            redisTemplate.opsForHash().delete(key, hashKeys);
            return true;
        } catch (Throwable t) {
            log.error("redis hash remove " + key + " has error,detail----" + t);
        }
        return false;
    }

    /**
     * @author:wanzhongsu
     * @description: 批量删除value通过key的表达式
     * @date:2019/8/12 11:05
     */
    public boolean removeMatch(String match) {
        Set<String> set = redisTemplate.keys(match);
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String keyStr = it.next();
            try {
                redisTemplate.delete(keyStr);
            } catch (Throwable t) {
                log.error("redis remove " + keyStr + " has error,detail----" + t);
                return false;
            }
        }
        return true;
    }


    /**
     * @author:wanzhongsu
     * @description: 批量删除value通过key的表达式
     * @date:2019/8/12 11:05
     */
    public int removeMatchInt(String match) {
        Set<String> set = redisTemplate.keys(match);
        Iterator<String> it = set.iterator();
        int count = 0;
        while (it.hasNext()) {
            String keyStr = it.next();
            try {
                redisTemplate.delete(keyStr);
                count = count + 1;
            } catch (Throwable t) {
                log.error("redis remove " + keyStr + " has error,detail----" + t);
            }
        }
        return count;
    }

    /**
     * @author:wanzhongsu
     * @description: 是否存在key
     * @date:2019/8/12 11:05
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Throwable t) {
            log.error("redis has key exec was error " + key + ", detail----" + t);
        }
        return false;
    }


    /**
     * @author:wanzhongsu
     * @description: 泛型对象（获取失败返回null）
     * @date:2019/8/12 11:04
     */
    public <T> T get(String key) {
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Throwable t) {
            log.error("redis get key exec was error " + key + ", detail----" + t);
        }
        return null;
    }


    /**
     * @author:wanzhongsu
     * @description: hash 获取hash对象
     * @date:2019/8/12 11:04
     */
    public <T> T getHash(String key) {
        try {
            return (T) redisTemplate.opsForHash().entries(key);
        } catch (Throwable t) {
            log.error("redis get hash key exec was error " + key + ", detail----" + t);
        }
        return null;
    }

    /**
     * @author:wanzhongsu
     * @description: 将hash对象转为list
     * @date:2019/8/12 11:04
     */
    public <T> T getHashOfList(String key) {
        try {
            return (T) redisTemplate.opsForHash().values(key);
        } catch (Throwable t) {
            log.error("redis get hash list key exec was error " + key + ", detail----" + t);
        }
        return null;
    }

    public <T> Long leftPush(String key, T value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public <T> T rightPop(String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    public Long queueSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public Long increment(String key, long base, long delta) {
        Object v = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(v)) {
            redisTemplate.opsForValue().set(key, base);
        }

        return redisTemplate.opsForValue().increment(key, delta);
    }

}
