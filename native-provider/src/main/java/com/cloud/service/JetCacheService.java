package com.cloud.service;

import com.alicp.jetcache.anno.Cached;
import com.cloud.entity.TbUrl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author yzz
 */
@Service
public class JetCacheService {

    @Autowired
    private TbUrlService tbUrlService;


    @Cached(name = "JetCacheService.getTbUrl", expire = 60)
    public List<TbUrl> getTbUrl() {
        List<TbUrl> list = tbUrlService.list();
        List<TbUrl> tbUrls = Optional.of(list).orElse(Lists.newArrayList());

        return tbUrlService.list();
    }


}
