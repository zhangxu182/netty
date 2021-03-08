package com.zhangjava.netty.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Http服务端
 *
 * @author zhangxu
 * @date 2021/3/8 13:40
 */
public class NettyHttpServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpChannelInitializer());
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            channelFuture.addListener(future -> {
               if (future.isSuccess()) {
                   System.out.println("端口绑定成功");
               } else {
                   System.out.println("端口绑定失败");
               }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }
    }
}
