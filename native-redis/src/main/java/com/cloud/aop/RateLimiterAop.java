package com.cloud.aop;

import com.alibaba.fastjson.JSON;
import com.cloud.vo.DataVO;
import com.cloud.limit.SimpleRateLimiter;
import com.cloud.util.IPUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RateLimiterAop {


    @Resource
    private HttpServletRequest request;


    SimpleRateLimiter limiter = new SimpleRateLimiter();

    @Pointcut("@annotation(com.cloud.annotation.RateLimiter)")
    public void rateLimiter() {
    }

    @Around(value = "rateLimiter()")
    public Object around(ProceedingJoinPoint joinPoint) {
        String ipAddress = IPUtil.getIpAddress(request);
        String name = joinPoint.getSignature().getName();
        if (!limiter.isActionAllowed(ipAddress, name, 60, 5)) {
            return JSON.toJSONString(new DataVO("110", "速度太快了，慢点！"));
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return JSON.toJSONString(new DataVO("110", "速度太快了，慢点！"));
    }


}
