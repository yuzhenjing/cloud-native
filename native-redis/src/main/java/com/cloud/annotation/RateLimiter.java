package com.cloud.annotation;

import java.lang.annotation.*;

/**
 * @author yzj
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 限制次数
     *
     * @return
     */
    int limit() default 5;

    /**
     * 超时时间
     *
     * @return
     */
    int timeout() default 1000;

    /**
     * 时间周期
     *
     * @return
     */
    int period() default 60;
}