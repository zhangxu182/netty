package com.zhangjava.netty.netty.codec.client;

import com.zhangjava.netty.netty.codec.StudentPOJO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
        StudentPOJO.Student.Builder student = StudentPOJO.Student.newBuilder();
        student.setId(1).setName("张三");

        // 向服务端发送消息
        ctx.writeAndFlush(student);
    }


    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
