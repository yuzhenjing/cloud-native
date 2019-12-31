package com.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class NacosController {

    @Autowired
    private Environment environment;

    @Value("${es.url}")
    private String value;

    @RequestMapping("/config/{name}")
    public String config(@PathVariable String name) {
        return environment.getProperty(name);
    }

    @RequestMapping("/config")
    public String value() {
        return this.value;
    }

}
