package com.cloud.lock;

import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author yzz
 */
@Component
public class DistributedLock {

    @Autowired
    private RedissonClient redissonClient;

    public void lock(String key) {
        RLock lock = redissonClient.getLock(key);
        lock.lock();


    }

    public boolean tryLock(String key) {
        RLock lock = redissonClient.getLock(key);
        return lock.tryLock();
    }


    @SneakyThrows
    public boolean tryLock(String key, long time, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        return lock.tryLock(time, time, unit);
    }


    public void unlock(String key) {
        RLock lock = redissonClient.getLock(key);
        lock.unlock();

    }
}
