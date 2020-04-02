package com.cloud.service;

import org.junit.Test;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class RetryTest {

    @Test
    public void test() throws Throwable {

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

        retryTemplate.execute(new RetryCallback<String, Throwable>() {

            @Override
            public String doWithRetry(RetryContext retryContext) throws Throwable {


                System.out.println("重试次数------" + retryContext.getRetryCount());
                throw new NullPointerException("NullPointerException");
            }
        }, new RecoveryCallback<String>() {
            @Override
            public String recover(RetryContext retryContext) throws Exception {
                return null;
            }
        });
    }
}
