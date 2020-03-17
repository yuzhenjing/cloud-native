package com.cloud.controller;

import com.cloud.annotation.RateLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateController {

    @RateLimiter
    @GetMapping("/test")
    public String test() {
        return "请求成功";
    }

    @RateLimiter
    @GetMapping("/test2")
    public String test2() {
        return "test2 请求成功";
    }
}
