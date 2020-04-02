package com.cloud.service;

import com.alibaba.fastjson.JSON;
import com.cloud.entity.RabbitOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class TradeService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private OrderService orderService;


    public String sendDelayQueue(Integer id) {

        RabbitOrder order = orderService.getById(id);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.setMandatory(true);
        MessageProperties properties = new MessageProperties();
        Message message = new Message(JSON.toJSONString(order).getBytes(), properties);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        correlationData.setReturnedMessage(message);
        if (id % 2 == 0) {
            properties.setExpiration((60 * 1000) + "");
            rabbitTemplate.convertAndSend("trade_direct_delay", "trade_delay_key_3m", message, correlationData);
        } else {
            rabbitTemplate.convertAndSend("trade_direct_delay", "trade_delay_key_2m", message, correlationData);
        }
        return "发送成功";
    }

    /**
     * 回调函数: confirm确认
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        assert correlationData != null;
        if (ack) {
            log.info("id={},消息投递{}", correlationData.getId(), "成功");
        } else {
            //消息投递失败 做补偿策略 保证最终一致性
            Message confirmMsg = correlationData.getReturnedMessage();
            assert confirmMsg != null;
            log.info("id={},消息投递{},消息体是：{}", correlationData.getId(), "失败", new String(confirmMsg.getBody()));
        }
    };

    /**
     * 回调函数: return返回
     */
    final RabbitTemplate.ReturnCallback returnCallback = (message, replyCode, replyText, exchange, routingKey) -> {
        //发送回调  处理
        log.info("消息内容是{}", new String(message.getBody()));
        log.info("replyCode{}", replyCode);
        log.info("replyText{}", replyText);
        log.info("exchange{}", exchange);
        log.info("routingKey{}", routingKey);
    };

}
