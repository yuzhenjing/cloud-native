package com.cloud.producter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Cleanup;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConfirmProducterDemo {


    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("47.110.59.121");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("cloud-native");
        try {
            @Cleanup Connection connection = factory.newConnection();
            @Cleanup Channel channel = connection.createChannel();

            //指定我们的消息投递模式: 消息的确认模式
            channel.confirmSelect();

            String msg = "hello Rabbit MQ send confirm message !";

            channel.basicPublish("order_exchange", "order_key", true, null, msg.getBytes());

            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("收到确认消息-------");
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息投递失败-------");
                }
            });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }


}
