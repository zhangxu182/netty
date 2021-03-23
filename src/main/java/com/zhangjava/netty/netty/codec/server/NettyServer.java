package com.zhangjava.netty.netty.codec.server;

import com.zhangjava.netty.netty.codec.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @author zhangxu
 * @date 2021/3/7 18:44
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 创建线程组
        // bossGroup处理连接请求，workerGroup处理业务
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建服务端，配置参数
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                // 设置线程组
                .group(bossGroup, workerGroup)
                // 设置服务器通道为NioServerSocketChannel
                .channel(NioServerSocketChannel.class)
                // 设置队列个数
                .option(ChannelOption.SO_BACKLOG, 128)
                // 设置保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 设置处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 加入解码器
                        pipeline.addLast(new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                        pipeline.addLast(new ServerHandler());
                    }
                });
        System.out.println("服务端初始化完成……");

        // 启动服务端
        ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口绑定成功");
            }
        });
        // 监听关闭通道
        channelFuture.channel().closeFuture().sync();
    }
}
