package com.cloud.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author yzz
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private Environment environment;

//    @Value("${redisson.redis.address]")
//    private String address;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://47.110.59.121:4096").setPassword("yy78789").setDatabase(0);
        return Redisson.create(config);
    }
}
