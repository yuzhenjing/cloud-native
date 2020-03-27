package com.cloud.demo.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Cleanup;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TxConsumerDemo {


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

            //开启事务
            channel.txSelect();

            channel.basicConsume("tx_queue", true,new FanoutConsumerHandler());


            channel.txCommit();

//            channel.txRollback();


        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }


}
