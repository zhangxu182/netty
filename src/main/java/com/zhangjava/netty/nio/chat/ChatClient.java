package com.zhangjava.netty.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * 群聊客户端
 *
 * @author zhangxu
 * @date 2021/3/4 21:46
 */
public class ChatClient {
    private static final int PORT = 6666;
    private static final String HOST = "127.0.0.1";
    private Selector selector;
    private SocketChannel socketChannel;

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        // 读取服务端的消息
        new Thread(() -> {
            while (true) {
                chatClient.read();
            }
        }).start();

        // 向服务器发送消息
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            chatClient.send(line);
        }
    }

    public ChatClient() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            // 设置非阻塞
            socketChannel.configureBlocking(false);

            // 创建选择器
            selector = Selector.open();
            // 注册到选择器上
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * 读取服务器的消息
     */
    public void read() {
        try {
            int select = selector.select();
            if (select > 0) {
                // 如果有事件，获取所有的selectionKeys
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    // 如果是读取事件
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        channel.configureBlocking(false);

                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        System.out.println(new String(buffer.array(), StandardCharsets.UTF_8).trim());
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * 向服务端发送消息
     */
    public void send(String msg) {
        try {
            msg = socketChannel.getLocalAddress() + "说：" + msg;
            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
            socketChannel.write(buffer);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
