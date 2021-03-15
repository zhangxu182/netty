package com.zhangjava.netty.netty.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * 群聊业务处理
 *
 * @author zhangxu
 * @date 2021/3/13 16:08
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * channel组，用来管理所有的客户端连接
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final String format = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        System.out.println("客户" + channel.remoteAddress() + " 加入群聊");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        System.out.println("客户" + channel.remoteAddress() + " 离开群聊");
    }

    /**
     * 客户端连接成功
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();

        // 群发上线通知
        channelGroup.writeAndFlush(getDateStr() + "[系统消息]" + channel.remoteAddress() + " 上线了\n");
        channelGroup.add(channel);
    }

    /**
     * 客户端断开连接
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();

        // 群发离线通知
        channelGroup.writeAndFlush(getDateStr() + "[系统消息]" + channel.remoteAddress() + " 离线了\n");
    }


    /**
     * 转发客户端消息
     *
     * @param ctx
     * @param msg
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                // 如果不是自己，转发消息
                ch.writeAndFlush(getDateStr() + "[用户" + ch.remoteAddress() + "说]" + msg.trim() + "\n");
            } else {
                // 如果是自己，回显自己的消息
                ch.writeAndFlush(getDateStr() + "[我说]" + msg.trim() + "\n");
            }
        });
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    /**
     * 获取格式化的时间字符串
     *
     * @return
     */
    private String getDateStr() {
        return DateFormatUtils.format(new Date(), format);
    }

}
