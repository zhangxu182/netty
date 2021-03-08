package com.zhangjava.netty.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 自定义http处理器
 * <p>
 * SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter子类
 * 客户端和服务端的通讯数据封装成 HttpObject 对象
 *
 * @author zhangxu
 * @date 2021/3/8 13:53
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        // 如果是HttpRequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("msg类型：" + msg.getClass());
            System.out.println("浏览器地址：" + ctx.channel().remoteAddress());

            // 过滤请求
            HttpRequest request = (HttpRequest) msg;
            String uri = request.uri();
            if ("/favicon.ico".equals(uri)) {
                System.out.println("请求 favicon.ico 不做响应");
                return;
            }

            // 向客户端（浏览器）返回消息 [http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello 浏览器", CharsetUtil.UTF_8);
            // 构建response对象
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            // 将构建好的对象返回
            ctx.writeAndFlush(response);
        }
    }
}
