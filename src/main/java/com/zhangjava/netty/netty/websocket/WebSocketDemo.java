package com.zhangjava.netty.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * netty websocket demo
 *
 * @author zhangxu
 * @date 2021/3/20 19:23
 */
public class WebSocketDemo {
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

                        // 加入http编解码器
                        pipeline.addLast(new HttpServerCodec());
                        // 增加块数据流处理器，用来处理以块传输的数据流
                        pipeline.addLast(new ChunkedWriteHandler());
                        // 将传输的多段数据聚合处理
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        /*
                         使用 ws://localhost:6666/hello来进行websocket连接
                         WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                         */
                        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                        // 增加自定义处理器
                        pipeline.addLast(new WebSocketHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(9527).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
