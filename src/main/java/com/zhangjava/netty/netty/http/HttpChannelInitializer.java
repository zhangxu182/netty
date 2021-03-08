package com.zhangjava.netty.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 自定义channel处理器初始化类
 *
 * @author zhangxu
 * @date 2021/3/8 13:46
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        // 获取管道
        ChannelPipeline pipeline = ch.pipeline();
        // 加入http编解码器
        //pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        pipeline.addLast(new HttpServerCodec());
        // 增加一个自定义处理器
        //pipeline.addLast("MyHttpServerHandler", new HttpServerHandler());
        pipeline.addLast(new HttpServerHandler());
    }
}
