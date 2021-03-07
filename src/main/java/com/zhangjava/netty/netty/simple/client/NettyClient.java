package com.zhangjava.netty.netty.simple.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端
 *
 * @author zhangxu
 * @date 2021/3/7 20:31
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        // 创建事件线程组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        // 创建服务端
        Bootstrap bootstrap = new Bootstrap()
                // 设置线程组
                .group(eventExecutors)
                // 设置通道为 NioSocketChannel
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    // 设置业务处理类
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

        System.out.println("客户端初始化完成……");
        // 连接服务端
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
        // 监听关闭事件
        channelFuture.channel().closeFuture().sync();

    }
}
