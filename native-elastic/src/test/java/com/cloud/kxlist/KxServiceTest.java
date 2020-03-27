package com.cloud.kxlist;

import com.cloud.kxlist.brand.KxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KxServiceTest {

    @Autowired
    private KxService kxService;

    @Test
    void initKxBrand() {
        kxService.initKxBrand();
    }

    @Test
    void initKxItem() {
        kxService.initKxItem();
    }
}