package com.cloud.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.util.Md5Utils;
import com.cloud.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class PreventRepetitionAspect {

    @Autowired
    private DistributedLock distributedLock;
    public static final String LOCK_KEY = "lock:key:";

    @Around(value = "@annotation(com.cloud.annotation.PreventRepetitionAnnotation)")
    public Object excute(ProceedingJoinPoint joinPoint) {
        String jsonString = JSON.toJSONString(joinPoint.getArgs());
        log.info("方法：【{}】执行参数：{}", jsonString, jsonString);
        //将字符串MD5加密
        String value = Md5Utils.getMD5(jsonString.getBytes());
        distributedLock.tryLock(LOCK_KEY + value);
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("程序执行出错：{}", throwable.getLocalizedMessage());
            return null;
        } finally {
            distributedLock.unlock(LOCK_KEY + value);
        }
    }


}
