package com.cloud.annotation;

import java.lang.annotation.*;

/**
 * @author yzz
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomRequestBody {
    boolean required() default true;
}
