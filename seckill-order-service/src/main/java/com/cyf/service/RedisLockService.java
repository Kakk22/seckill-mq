package com.cyf.service;

/**
 * redis分布式锁实现
 *
 * @author 陈一锋
 * @date 2021/4/1 21:51
 **/
public interface RedisLockService {


    /**
     * 获取锁
     *
     * @param key   键
     * @param value 值
     * @param count 获取锁尝试次数
     * @return 是否获取锁
     */
    boolean lock(String key, String value, int count);

    /**
     * 使用lua脚本解锁
     *
     * @param key   键
     * @param value 值
     */
    void unLock(String key, String value);
}
