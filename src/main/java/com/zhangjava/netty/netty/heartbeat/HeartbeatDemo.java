package com.zhangjava.netty.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * netty心跳机制演示
 *
 * @author zhangxu
 * @date 2021/3/19 21:10
 */
public class HeartbeatDemo {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        /*
                            1.IdleStateHandler是netty提供的处理空闲状态的处理器
                            2.long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包
                            3.long writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包
                            4.long allIdleTime：表示多长时间没有读写，就会发送一个心跳检测包
                         */
                        pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                        /*
                        当IdleStateEvent触发后，就会传递给管道的下一个handler去处理
                        通过调用下一个handler的userEventTiggered方法去处理
                         */
                        pipeline.addLast(new HeartHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
