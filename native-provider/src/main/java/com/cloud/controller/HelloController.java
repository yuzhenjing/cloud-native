package com.cloud.controller;

import com.cloud.api.HelloFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yzj
 * @description TODO
 */
@Slf4j
@RestController
public class HelloController {


    @Reference(check = false)
    private HelloFacade helloFacade;


    @RequestMapping(value = {"/hello/{name}", "/hello"})
    public String hello(@PathVariable(required = false) String name, String value) {
        log.info("名字是：{}", name);
        System.out.println(value);
        return helloFacade.hello(name);
    }
}
