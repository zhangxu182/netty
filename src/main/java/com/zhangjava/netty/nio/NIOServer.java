package com.zhangjava.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * ServerSocketChannel 示例
 *
 * @author zhangxu
 * @date 2021/3/2 21:22
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(6666));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 创建Selector对象
        Selector selector = Selector.open();
        // 将ServerSocketChannel 注册到Selector上，绑定连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 监听事件，如果>0，表示有事件发生
            if (selector.select() > 0) {
                // 获取事件集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 遍历事件集合，处理事件
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey selectionKey = keyIterator.next();
                    // 有新的客户端连接
                    if (selectionKey.isAcceptable()) {
                        // 获取新连接
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        InetSocketAddress remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
                        System.out.println(remoteAddress.getAddress() + "上线了");
                        // 设置为非阻塞
                        socketChannel.configureBlocking(false);
                        // 注册到Selector上，绑定读取事件，并设置缓冲区
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }

                    // 如果是读取事件
                    if (selectionKey.isReadable()) {
                        // 获取事件关联的channel
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        // 获取channel关联的Buffer
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        // 将通道的数据读取到buffer
                        channel.read(buffer);
                        InetSocketAddress address = (InetSocketAddress) channel.getRemoteAddress();
                        System.out.println(address.getAddress() + "说：" + new String(buffer.array(), StandardCharsets.UTF_8));
                    }

                    // 清除处理过的事件，防止重复处理
                    keyIterator.remove();
                }
            }
        }


    }
}
