package com.cloud.service;

import com.alibaba.fastjson.JSON;
import com.cloud.entity.RabbitOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BizOrderService {


    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public String createOrder() {

        for (int i = 0; i < 10000; i++) {

            RabbitOrder order = new RabbitOrder();
            order.setOrderMoney(i * 100L);
            order.setOrderNum(System.currentTimeMillis());
            order.setUserId(UUID.randomUUID().toString());
            order.setUserName("张三" + i);
            order.setPayStatus(1);

            orderService.save(order);

            rabbitTemplate.convertAndSend("order_exchange", "order_key", JSON.toJSONString(order));

        }


        return "成功";

    }


}
