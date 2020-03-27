package com.cloud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderAsyncService {


    @RabbitListener(queues = {"order_queue"})
    public void listener(Message message) {
        log.info("订单支付过后 添加销售动态提醒");

    }

    @RabbitListener(queues = {"order_queue"})
    public void listener2(Message message) {
        log.info("订单支付过后 扣减库存");
    }

    @RabbitListener(queues = {"order_queue"})
    public void listener3(Message message) {
        log.info("订单支付过后 增加积分");
    }

    @RabbitListener(queues = {"order_queue"})
    public void listener4(Message message) {
        log.info("订单支付过后 发送短信");
    }

    @RabbitListener(queues = {"order_queue"})
    public void listener5(Message message) {
        log.info("订单支付过后 推送ERP");
    }


}
