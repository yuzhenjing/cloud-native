package com.cloud.service;

import com.cloud.api.HelloFacade;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class HelloService {


    @Reference
    private HelloFacade helloFacade;

    public String test(String name) {
        return helloFacade.hello(name);
    }


}
