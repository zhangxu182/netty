package com.zhangjava.netty.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 群聊服务端
 *
 * @author zhangxu
 * @date 2021/3/4 20:29
 */
public class ChatServer {
    private static final int PORT = 6666;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        // 监听客户端连接
        chatServer.listen();
    }

    public ChatServer() {
        try {
            // 创建ServerSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            // 设置非阻塞
            serverSocketChannel.configureBlocking(false);
            // 监听端口
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            // 创建选择器
            selector = Selector.open();
            // 将ServerSocketChannel注册到选择器上，绑定连接事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * 监听客户端连接
     */
    public void listen() {
        try {
            while (true) {
                System.out.println("等待客户端连接……");
                int select = selector.select();
                // 表示有事件发生
                if (select > 0) {
                    // 获取selectionKeys列表
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    // 处理selectionKeys
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        // 根据事件类型进行处理
                        if (selectionKey.isAcceptable()) {
                            // 连接事件
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);

                            // 上线提醒
                            System.out.println(socketChannel.getRemoteAddress() + "上线了");
                        }
                        if (selectionKey.isReadable()) {
                            // 读取事件
                            readData(selectionKey);
                        }
                        // 删除处理过的selectionKey，防止重复处理消息
                        keyIterator.remove();
                    }
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * 读取客户端的数据
     *
     * @param selectionKey
     */
    public void readData(SelectionKey selectionKey) {
        // 获取通道
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        // 创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int read = channel.read(buffer);
            if (read > 0) {
                // 如果读取到了数据
                String msg = new String(buffer.array(), StandardCharsets.UTF_8);
                System.out.println(msg.trim());

                // 消息转发到其他客户端
                forwardMsg(msg, channel);
            }
        } catch (IOException ioException) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了");
                // 取消注册
                selectionKey.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 群发消息
     *
     * @param msg            消息内容
     * @param excludeChannel 需要排除的通道
     */
    public void forwardMsg(String msg, SocketChannel excludeChannel) throws IOException {
        System.out.println("服务器消息转发……");
        // 获取所有的通道
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            Channel channel = key.channel();
            if (channel instanceof SocketChannel && excludeChannel != channel) {
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                SocketChannel socketChannel = (SocketChannel) channel;
                socketChannel.write(buffer);
            }
        }
    }

}
