package com.zhangjava.netty.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * 处理业务
 *
 * @author zhangxu
 * @date 2021/3/20 19:50
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        System.out.println("服务器收到了消息：" + msg.text());

        // 回复消息
        TextWebSocketFrame textWebSocketFrame =
                new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + ":" + msg.text());
        ctx.channel().writeAndFlush(textWebSocketFrame);
    }

    /**
     * 当连接后触发
     *
     * @param ctx
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("handlerAdded：" + ctx.channel().id());
    }

    /**
     * 断开连接后触发
     *
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("handlerRemoved：" + ctx.channel().id());
    }

    /**
     * 处理异常
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
