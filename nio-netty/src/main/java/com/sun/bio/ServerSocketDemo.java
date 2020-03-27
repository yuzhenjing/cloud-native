package com.sun.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ServerSocketDemo {


    public static void main(String[] args) throws IOException {

        ExecutorService executor = Executors.newCachedThreadPool();


        //创建服务端
        ServerSocket serverSocket = new ServerSocket(9999);


        while (true) {
            //获取客户端链接
            Socket socket = serverSocket.accept();
            log.info("客户端链接成功：{}", socket.getInetAddress().getHostAddress());

            new ServerHandler(socket).start();

        }


    }

    static class ServerHandler extends Thread {

        private Socket socket;

        public ServerHandler(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {

            try {
                //获取客户端输入的消息
                InputStream inputStream = socket.getInputStream();

                log.info("打印客户端输入的消息流：{}", inputStream);

                //将消息流转化为字符串输出


                while (true) {
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        String message = new String(bytes, 0, len);
                        log.info("客户端输入的信息内容是：{}", message);
                        //回传消息到客户端
                        socket.getOutputStream().write("客户端 我收到你的消息啦".getBytes());
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

}
