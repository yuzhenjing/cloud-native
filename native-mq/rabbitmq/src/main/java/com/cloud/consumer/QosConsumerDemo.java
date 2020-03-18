package com.cloud.consumer;

import com.rabbitmq.client.*;
import lombok.Cleanup;

import java.io.IOException;

public class QosConsumerDemo {

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

            //服务质量保证，在非自动确认情况下，一定数目的消息没有确认，不进行消费新的消息，通过producer/consumer设置qos的值
            channel.basicQos(0, 1, false);

            channel.basicConsume("test_queue", false, new Consumer() {
                @Override
                public void handleConsumeOk(String consumerTag) {

                }

                @Override
                public void handleCancelOk(String consumerTag) {

                }

                @Override
                public void handleCancel(String consumerTag) throws IOException {

                }

                @Override
                public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

                }

                @Override
                public void handleRecoverOk(String consumerTag) {

                }

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.err.println("-----------consume message----------");
                    System.err.println("body: " + new String(body));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
