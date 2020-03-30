package com.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.entity.RabbitOrder;
import com.cloud.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/rabbit")
public class OrderController {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private OrderService orderService;


    @GetMapping("/delay/msg")
    public String delayMsg() {

        RabbitOrder order = orderService.getById(1);

        log.info("消息发送时间是：{}", LocalDateTime.now().toString());
        rabbitTemplate.convertAndSend("delayedExchange", "delayed", JSON.toJSONString(order), message -> {
            message.getMessageProperties().setHeader("x-delay", 3000);
            return message;
        });
        boolean annotationPresent = this.getClass().isAnnotationPresent(Component.class);
        System.out.println(annotationPresent);
        return "消息发送成";
    }

}
