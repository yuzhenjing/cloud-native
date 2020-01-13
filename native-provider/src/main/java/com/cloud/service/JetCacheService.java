package com.cloud.service;

import com.alicp.jetcache.anno.Cached;
import com.cloud.entity.TbUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzz
 */
@Service
public class JetCacheService {

    @Autowired
    private TbUrlService tbUrlService;


    @Cached(name = "JetCacheService.getTbUrl", expire = 60)
    public List<TbUrl> getTbUrl() {
        return tbUrlService.list();
    }


}
