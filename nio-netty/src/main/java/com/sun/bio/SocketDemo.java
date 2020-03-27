package com.sun.bio;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SocketDemo {


    public static void main(String[] args) throws IOException {

        ExecutorService service = Executors.newFixedThreadPool(5);

        Socket socket = new Socket("localhost", 9999);

        service.execute(() -> {
            for (int i = 0; i < 1000; i++) {
                String message = "第【" + i + "】号消息发送";
                log.info("第【{}】号消息发送", i);
                try {



                    socket.getOutputStream().write(message.getBytes());

                    service.execute(() -> {
                        String recved = null;
                        try {
                            recved = IOUtils.toString(socket.getInputStream(), Charset.defaultCharset());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        log.info("客户端收到服务器的数据啦：{}", recved);
                    });




                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
