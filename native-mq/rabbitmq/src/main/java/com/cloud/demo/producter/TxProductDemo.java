package com.cloud.demo.producter;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Cleanup;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TxProductDemo {


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


            channel.queueDeclare("tx_queue", true, false, false, null);

            channel.exchangeDeclare("tx_exchange", BuiltinExchangeType.FANOUT);

            channel.queueBind("tx_queue", "tx_exchange", "tx_001");

            channel.basicPublish("tx_exchange", "tx_001", null, "测试事务".getBytes());
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }


    }


}
