package com.cloud.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yzj
 */
@Configuration
public class RabbitmqQueueConfig {

    @Bean
    public Queue directQueue() {
        return new Queue("direct_queue");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct_exchange");
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("direct_routingKey");
    }

    @Bean
    public Queue fountQueue() {
        return new Queue("order_queue");
    }

    @Bean
    public Queue orderA() {
        return new Queue("orderA");
    }

    @Bean
    public Queue orderB() {
        return new Queue("orderB");
    }

    @Bean
    public Queue orderC() {
        return new Queue("orderC");
    }

    @Bean
    public Queue orderD() {
        return new Queue("orderD");
    }

    @Bean
    public Queue orderF() {
        return new Queue("orderF");
    }

    @Bean
    public Queue orderE() {
        return new Queue("orderE");
    }

    @Bean
    public FanoutExchange fountExchange() {
        return new FanoutExchange("order_exchange");
    }

    @Bean
    public Binding fountBinding() {
        return BindingBuilder.bind(orderA()).to(fountExchange());
    }

    @Bean
    public Binding bindingA() {
        return BindingBuilder.bind(orderA()).to(fountExchange());
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(orderB()).to(fountExchange());
    }

    @Bean
    public Binding bindingC() {
        return BindingBuilder.bind(orderC()).to(fountExchange());
    }

    @Bean
    public Binding bindingD() {
        return BindingBuilder.bind(orderD()).to(fountExchange());
    }

    @Bean
    public Binding bindingE() {
        return BindingBuilder.bind(orderE()).to(fountExchange());
    }

    @Bean
    public Binding bindingF() {
        return BindingBuilder.bind(orderF()).to(fountExchange());
    }

    @Bean
    public Queue topicQueue() {
        return new Queue("order_topic");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("order_exchange_topic");
    }

    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with("order_key");
    }


    @Bean
    public DirectExchange delayedExchange() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-delayed-type", "direct");
        return new DirectExchange("delayedExchange", true, false, map);

    }

    @Bean
    public Queue delayedQueue() {
        return new Queue("delayedQueue");
    }

    @Bean
    public Binding delayedBinding() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with("delayed");
    }












}
