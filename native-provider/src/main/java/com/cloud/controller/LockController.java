package com.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.entity.TbUrl;
import com.cloud.service.TbUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Executor;

@RestController
public class LockController {

    @Autowired
    private TbUrlService tbUrlService;

    @Qualifier("taskExecutor")
    @Autowired
    private Executor executor;


    @RequestMapping("/lock/add")
//    @PreventRepetitionAnnotation
    public TbUrl add(TbUrl tbUrl, HttpServletRequest request) {

        System.out.println(JSON.toJSONString(request.getParameterMap()));

        executor.execute(() -> {
            tbUrlService.save(tbUrl);
        });


        return tbUrl;
    }


}
