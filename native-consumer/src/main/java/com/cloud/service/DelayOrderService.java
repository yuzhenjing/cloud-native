package com.cloud.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
public class DelayOrderService {



    @RabbitListener(queues = "trade_queue")
    public void tradeQueueMessage(Message message, Channel channel) {
        log.info("queue=[trade_queue]收到消息：{}", new String(message.getBody()));
        MessageProperties properties = message.getMessageProperties();
        //如果向MQ确认的时候失败
        //如果是业务失败： 直接ACK成功  记录失败信息
        try {
            channel.basicAck(properties.getDeliveryTag(), false);
        } catch (IOException e) {
            log.error("确认消息失败,消息体是：{}", JSON.toJSONString(message.getBody()));
            try {
                channel.basicNack(properties.getDeliveryTag(), false, true);
            } catch (IOException ex) {
                log.error("消息确认失败，重回队列失败：{}", ex.getMessage());
            }

        }
    }

}