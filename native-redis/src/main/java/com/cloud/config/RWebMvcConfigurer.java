package com.cloud.config;


import com.cloud.annotation.RateLimiter;
import com.cloud.limit.SimpleRateLimiter;
import com.cloud.util.IPUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author yzj
 */
//@Configuration
public class RWebMvcConfigurer implements WebMvcConfigurer {


    SimpleRateLimiter limiter = new SimpleRateLimiter();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

                final String ipAddress = IPUtil.getIpAddress(request);

                HandlerMethod handlerMethod = (HandlerMethod) handler;

                Method method = handlerMethod.getMethod();

                RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
                if (rateLimiter != null) {
                    int limit = rateLimiter.limit();
                    int period = rateLimiter.period();
                    if (!limiter.isActionAllowed(ipAddress, method.getName(), period, limit)) {
                        response.setContentType("application/json; charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.print("请求太过频繁，稍后重试");
                        writer.close();
                        response.flushBuffer();
                    }
                }
                return true;
            }
        });

    }
}
