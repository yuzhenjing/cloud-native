package com.cloud.limit;

import redis.clients.jedis.Jedis;

public class RateLimitTest {


    public static void main(String[] args) {

        Jedis jedis = new Jedis("47.110.59.121", 4096);
        jedis.auth("yy78789");
        SimpleRateLimiter limiter = new SimpleRateLimiter(jedis);
        for (int i = 0; i < 20; i++) {
            boolean actionAllowed = limiter.isActionAllowed("www", "com", 10, 5);
            System.out.println(actionAllowed);
        }


    }


}
