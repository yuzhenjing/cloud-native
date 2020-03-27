package com.cloud.demo.producter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectProducterDemo {


    public static final String EXCHANGE = "test_exchange";


    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("47.110.59.121");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        Connection connection = null;
        Channel channel = null;
        try {
            //获取与RabbitMQ服务器的链接
            connection = factory.newConnection();

            //获取传输通道
            channel = connection.createChannel();


            /*
             * 声明（创建）队列
             * 参数1：队列名称
             * 参数2：为true时server重启队列不会消失
             * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常
             * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息)
             * 参数5：建立队列时的其他参数
             */
            channel.queueDeclare("test_queue", false, false, false, null);

//            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT);


            for (int i = 0; i < 10000L; i++) {

                //channel.txSelect(); // 声明事务

                channel.basicPublish("", "test_queue", null, (System.currentTimeMillis() + "第【" + i + "】号消息").getBytes());
                Thread.sleep(1000);
//                channel.txCommit(); 提交事务
//                channel.txRollback(); 回滚事务

            }

        } catch (IOException | TimeoutException | InterruptedException e) {
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
