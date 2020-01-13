package com.cloud.controller;

import com.cloud.entity.TbUrl;
import com.cloud.service.JetCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JetCacheController {

    @Autowired
    private JetCacheService jetCacheService;

    @RequestMapping(value = "/list/url")
    public List<TbUrl> listUrl() {
        return jetCacheService.getTbUrl();
    }

}
