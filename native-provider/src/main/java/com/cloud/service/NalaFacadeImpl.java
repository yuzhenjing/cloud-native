package com.cloud.service;

import com.cloud.api.NalaFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author yzj
 * @description TODO
 */
@Slf4j
@Service
public class NalaFacadeImpl implements NalaFacade {
    @Override
    public String nala(String num) {
        log.info("正常实现------");
        return num;
    }
}
