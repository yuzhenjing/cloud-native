package com.cloud.service;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
public class OrderAsyncService {


    @RabbitListener(queues = {"orderF"})
    public void listener(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("QUEUE-->orderF 消息内容是：{}", msg);
        autoAck(channel, message);
        log.info("订单支付过后 添加销售动态提醒");
    }

    @RabbitListener(queues = {"order_queue"})
    public void listener2(Message message, Channel channel) {
        log.info("订单支付过后 扣减库存");
        autoAck(channel, message);
    }

    @RabbitListener(queues = {"orderB"})
    public void listener3(Message message, Channel channel) {
        autoAck(channel, message);
        log.info("订单支付过后 增加积分");
    }

    @RabbitListener(queues = {"orderC"})
    public void listener4(Message message, Channel channel) {
        autoAck(channel, message);
        log.info("订单支付过后 发送短信");
    }

    @RabbitListener(queues = {"orderD"})
    public void listener5(Message message, Channel channel) {
        autoAck(channel, message);
        log.info("订单支付过后 推送ERP");
    }

    @RabbitListener(queues = {"delayedQueue"})
    public void listener6(Message message, Channel channel) {
        autoAck(channel, message);

        log.info("收到延迟投递消息，时间是：{}", LocalDateTime.now().toString());
    }


    public void autoAck(Channel channel, Message message) {
        try {
            MessageProperties properties = message.getMessageProperties();
            channel.basicAck(properties.getDeliveryTag(), false);
        } catch (IOException e) {
            log.error("消费端 ACK 出错");
        }
    }


}
