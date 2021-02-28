package com.zhangjava.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO模型，服务端
 *
 * @author zhangxu
 * @date 2021/2/27 17:21
 */
public class BioService {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);

            while (true) {
                // 阻塞监听客户端的连接
                System.out.println("等待客户端连接");
                Socket socket = serverSocket.accept();
                System.out.println("客户端" + socket.getInetAddress() + "连接了");
                // 创建线程处理客户端交互
                Thread thread = new Thread(() -> {
                    try {
                        InputStream inputStream = socket.getInputStream();
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(bytes)) > 0) {
                            System.out.println(socket.getInetAddress() + "说：" + new String(bytes, 0, len));
                        }

                    } catch (IOException ioException) {
                        System.out.println("客户端" + socket.getInetAddress() + "已离开");
                    }
                });
                thread.start();
            }

        } catch (IOException ioException) {
            System.out.println("服务端启动失败");
            ioException.printStackTrace();
        }
    }
}
