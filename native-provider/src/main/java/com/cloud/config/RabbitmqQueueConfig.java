package com.cloud.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public DirectExchange fountExchange() {
        return new DirectExchange("order_exchange");
    }

    @Bean
    public Binding fountBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("order_key");
    }



}
