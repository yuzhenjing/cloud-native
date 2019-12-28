package com.cloud.controller;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yzj
 */
@RestController
public class AmqpController {


    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @RequestMapping("/send/{message}")
    public String sendMsg(@PathVariable String message) {
        rabbitMessagingTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", message);
        return message;
    }


}
