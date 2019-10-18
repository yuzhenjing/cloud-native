package com.cloud.api;

import com.cloud.service.FallBackNalaFacadeImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author yzj
 * @description TODO
 */
@FeignClient(value = "nala-facade", fallback = FallBackNalaFacadeImpl.class, configuration = NalaFacade.FeignConfiguration.class)
public interface NalaFacade {


    @GetMapping("/nala/{num}")
    String nala(@PathVariable String num);

    class FeignConfiguration {
        @Bean
        public FallBackNalaFacadeImpl echoServiceFallback() {
            return new FallBackNalaFacadeImpl();
        }
    }
}
