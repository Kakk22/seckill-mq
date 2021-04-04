package com.cyf.service.impl;

import com.cyf.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈一锋
 * @date 2021/4/1 21:56
 **/
@Slf4j
@Service
public class RedisLockServiceImpl implements RedisLockService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean lock(String key, String value, int count) {
        do {
            count--;
            boolean bool = redisTemplate.opsForValue().setIfAbsent(key, value, 30, TimeUnit.SECONDS);
            if (bool) {
                return true;
            }
            try {
                TimeUnit.MICROSECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("分布式锁发生异常:{}", e.getMessage());
            }
        } while (count >= 0);

        log.info(Thread.currentThread().getName() + "获取分布式锁失败");
        return false;
    }

    @Override
    public void unLock(String key, String value) {

        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";

        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList(key), value);

        if (!Objects.isNull(result)) {
            log.info("redis删除分布式锁成功,当前线程:{}", Thread.currentThread().getName());
        }
    }
}
