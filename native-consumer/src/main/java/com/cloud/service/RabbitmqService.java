package com.cloud.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqService {


    @RabbitListener(queues = {"direct_queue"})
    public void listener(Message message) {
        System.out.println("1--消费到了" + new String(message.getBody()));
    }

    @RabbitListener(queues = {"direct_queue"})
    public void listener2(Message message) {
        System.out.println("2--消费到了" + new String(message.getBody()));
    }


}
