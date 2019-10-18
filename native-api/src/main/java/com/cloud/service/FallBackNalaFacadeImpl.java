package com.cloud.service;

import com.cloud.api.NalaFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yzj
 * @description TODO
 */
@Slf4j
@Service
public class FallBackNalaFacadeImpl implements NalaFacade {
    @Override
    public String nala(String num) {
        log.info("进入了 失败回调");
        return num;
    }
}
