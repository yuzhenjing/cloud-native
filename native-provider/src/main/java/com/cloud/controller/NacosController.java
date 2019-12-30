package com.cloud.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RefreshScope
@RestController
public class NacosController {


    @Value("${es.url}")
    private String es_url;

    @Autowired
    private Environment environment;

    @RequestMapping("/env/{name}")
    public String getEnvByName(@PathVariable String name) {
        return environment.getProperty(name);
    }

    @RequestMapping("/es")
    public String getConfig() {
        return this.es_url;
    }


}
