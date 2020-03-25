package com.cloud.demo.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Cleanup;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanoutConsumerDemo {


    public static void main(String[] args) {


        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("47.110.59.121");

        factory.setPort(5672);

        factory.setUsername("guest");

        factory.setPassword("guest");

        factory.setVirtualHost("/");

        try {
            @Cleanup Connection connection = factory.newConnection();

            @Cleanup Channel channel = connection.createChannel();
            while (true) {
                channel.basicConsume("test_queue", true, new FanoutConsumerHandler());
            }


        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }


    }


}
