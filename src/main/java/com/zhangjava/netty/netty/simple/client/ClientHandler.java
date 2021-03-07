package com.zhangjava.netty.netty.simple.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 客户端自定义处理类
 *
 * @author zhangxu
 * @date 2021/3/7 20:44
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 连接成功事件回调
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 向服务端发送消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello服务端", CharsetUtil.UTF_8));
    }

    /**
     * 读取服务端的消息
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务端说：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务端IP：" + ctx.channel().remoteAddress());
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
