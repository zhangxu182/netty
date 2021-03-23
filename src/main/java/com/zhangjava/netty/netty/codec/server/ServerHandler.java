package com.zhangjava.netty.netty.codec.server;

import com.zhangjava.netty.netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 自定义业务处理器
 *
 * @author zhangxu
 * @date 2021/3/7 19:47
 */
public class ServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) {
        System.out.println(msg.getId());
        System.out.println(msg.getName());
    }

    /**
     * 客户端消息处理完毕的回调
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 写出消息到客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端", CharsetUtil.UTF_8));
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 关闭通道
        ctx.close();
    }


}
