package com.zhangjava.netty.netty.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 群聊服务端
 *
 * @author zhangxu
 * @date 2021/3/13 15:47
 */
public class NettyChatServer {
    private static Logger log = LoggerFactory.getLogger(NettyChatServer.class);

    /**
     * 监听端口号
     */
    private final int port;

    public NettyChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        NettyChatServer nettyChatServer = new NettyChatServer(8888);
        nettyChatServer.start();
    }

    /**
     * 启动
     */
    public void start() {
        // 创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 设置服务端启动参数
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 256)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 设置自定义业务处理类
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 加入解码器
                        pipeline.addLast(new StringDecoder());
                        // 加入编码器
                        pipeline.addLast(new StringEncoder());
                        // 加入业务处理类
                        pipeline.addLast(new ChatServerHandler());
                    }
                });

        try {
            System.out.println("服务端初始化完成");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("服务端启动失败", e);
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}