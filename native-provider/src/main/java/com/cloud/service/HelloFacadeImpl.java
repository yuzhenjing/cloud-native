package com.cloud.service;

import com.cloud.api.HelloFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author yzj
 * @description TODO
 */
@Slf4j
@Service
public class HelloFacadeImpl implements HelloFacade {


    @Override
    public String hello(String name) {
        log.info("--------------");
        test();
        return "你好 " + name;
    }


    public void test() {
        log.info("--------------");
    }


}
