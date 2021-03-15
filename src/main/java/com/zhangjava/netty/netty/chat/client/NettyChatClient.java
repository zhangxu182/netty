package com.zhangjava.netty.netty.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author zhangxu
 * @date 2021/3/13 15:48
 */
public class NettyChatClient {
    private final int port;
    private final String ip;

    public NettyChatClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        NettyChatClient chatClient = new NettyChatClient("127.0.0.1", 8888);
        chatClient.start();
    }

    public void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 增加编码解码器
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        // 自定义业务处理类
                        pipeline.addLast(new ChatClientHandler());
                    }
                });
        try {
            System.out.println("客户端初始化完成");
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            Channel channel = channelFuture.channel();

            // 控制台写数据
            writeConsole(channel);
        } catch (InterruptedException e) {
            group.shutdownGracefully();
        }
    }

    public static void writeConsole(Channel channel) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                channel.writeAndFlush(line);
            }
        }
    }

}
