package com.cloud.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class BizRabbitOrderServiceTest {

    @Autowired
    private BizOrderService bizOrderService;

    @Test
    void createOrder() {

        bizOrderService.createOrder();


    }

    @Test
    void test() throws IOException, TimeoutException {

        bizOrderService.test();


    }
}