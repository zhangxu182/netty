package com.zhangjava.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * SocketChannel示例
 *
 * @author zhangxu
 * @date 2021/3/2 22:19
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞
        socketChannel.configureBlocking(false);
        // 服务器地址
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
        // 如果连接到了服务端
        if (!socketChannel.connect(address)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("正在连接服务端……");
            }
        }

        String str = "hello world";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer);
        System.in.read();
    }
}
