package com.cloud.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BizRabbitOrderServiceTest {

    @Autowired
    private BizOrderService bizOrderService;
    @Test
    void createOrder() {

        bizOrderService.createOrder();


    }
}