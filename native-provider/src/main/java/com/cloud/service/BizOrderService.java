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

import java.util.List;
import java.util.UUID;

@Slf4j
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


        }


        return "成功";

    }


    public void test() {
        List<RabbitOrder> list = orderService.list();
        list.forEach(ro -> {
            try {
                this.send(JSON.toJSONString(ro));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    /**
     * 回调函数: confirm确认
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        log.info("id={},消息投递{}", correlationData.getId(), ack ? "成功" : "失败");
        if (ack) {
            log.info("//更新数据库，可靠性投递机制");
        } else {
            log.info("//可以进行日志记录、异常处理、补偿处理等");
        }
    };

    /**
     * 回调函数: return返回
     */
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText,
                                    String exchange, String routingKey) {

            //发送回调  处理
            log.info("消息内容是{}", message);


            System.err.println("return exchange: " + exchange + ", routingKey: "
                    + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
        }
    };

    /**
     * 发送消息方法调用: 构建Message消息
     *
     * @param jsonMsg
     * @throws Exception
     */
    public void send(String jsonMsg) {


        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        MessageProperties properties = new MessageProperties();

        Message message = new Message(jsonMsg.getBytes(), properties);

        //id + 时间戳 全局唯一 做补偿策略的时候，必须保证这是全局唯一的消息
        CorrelationData correlationData = new CorrelationData(System.currentTimeMillis() + "");
        correlationData.setReturnedMessage(message);
        rabbitTemplate.convertAndSend("order_exchange", "order_key", message, correlationData);
    }
}
