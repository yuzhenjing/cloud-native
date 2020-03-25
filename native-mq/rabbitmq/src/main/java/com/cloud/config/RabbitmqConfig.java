package com.cloud.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {


    @Bean
    public Queue queue() {
        //autoDelete 表示当没有消费者与此建立连接后删除该队列
        //exclusive 表示独占 用于顺序消费
        return new Queue("boot_queue", true, false, false);
    }

    @Bean
    public Exchange exchange() {
        //autoDelete 当交换机上绑定的最后一个队列被删除时候 删除该交换机
        return new FanoutExchange("boot_exchange", true, false, null);
    }

    @Bean
    public Exchange directExchange() {
        //Direct 所有发送到Direct Exchange 的消息将会被发送到RouteKey 中指定的队列中去
        return new DirectExchange("boot_direct_exchange", true, false);
    }


    @Bean
    public Binding binding() {
        return new Binding("", Binding.DestinationType.EXCHANGE, "boot_exchange", "boot_key01", null);
    }



}
