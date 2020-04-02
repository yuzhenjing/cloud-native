package com.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryTemplateConfig {


    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(6);
        //设置重试策略
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        // 1. 指数退避策略 半小时重试10 次  间隔重试   4s 16s 64s 256s 1024s
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(4);
        backOffPolicy.setMaxInterval(1024 * 1000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }
}
