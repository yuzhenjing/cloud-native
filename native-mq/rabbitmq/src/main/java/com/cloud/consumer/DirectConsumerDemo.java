package com.cloud.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectConsumerDemo {


    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/");
        factory.setHost("47.110.59.121");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = null;

        Channel channel = null;


        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare("test_queue", false, false, false, null);

//            channel.queueBind("queue_test", "test_exchange", "key_1");


            while (true) {
                channel.basicConsume("test_queue", true, new Consumer() {
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
                        System.out.println("收到消息：" + consumerTag);
                    }

                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println("收到消息：" + new String(body));
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
