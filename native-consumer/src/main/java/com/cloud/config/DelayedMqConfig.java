package com.cloud.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class DelayedMqConfig {


    /**
     * 申明一个延迟队列 给整个队列设置消息过期时间 x-message-ttl 2分钟
     * 当消息到达过期时间的时候 rabbitmq 会将小修从新定位转发打破其他队列中 本例是转发到
     * exchange :trade_exchange routing_key trade_key
     *
     * @return
     */
    @Bean
    public Queue delay_queue() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x-message-ttl", 1200000);
        map.put("x-dead-letter-exchange", "trade_exchange");
        map.put("x-dead-letter-routing-key", "trade_delay_key_2m");
        return new Queue("delay_queue", true, false, false, map);
    }

    /**
     * 申明一个延迟队列，在发送消息的时候给消息设置过期时间 3分钟
     * 当消息达到过期时间的时候，rabbitmq将会把消息重新定位转发到其它的队列中去,本例子转发到
     * exchange:trade_exchange
     * routing-key:trade_key
     * 满足如上两点的队列中去即为：trade_queue
     *
     * @return
     */
    @Bean
    public Queue delay_message() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", "trade_exchange");
        map.put("x-dead-letter-routing-key", "trade_delay_key_3m");
        return new Queue("delay_message", true, false, false, map);
    }


    @Bean
    public DirectExchange trade_direct_delay() {
        return new DirectExchange("trade_direct_delay");
    }


    @Bean
    public Binding delay_queue_binding() {
        return BindingBuilder.bind(delay_queue()).to(trade_direct_delay()).with("trade_delay_key");
    }

    @Bean
    public Binding delay_message_binding() {
        return BindingBuilder.bind(delay_message()).to(trade_direct_delay()).with("trade_delay_key");
    }


    @Bean
    public Queue trade_queue() {
        return new Queue("trade_queue");
    }

    @Bean
    public DirectExchange trade_exchange() {
        return new DirectExchange("trade_exchange");
    }

    @Bean
    public Binding trade_binding() {
        return BindingBuilder.bind(trade_queue()).to(trade_exchange()).with("trade_key");
    }


}
