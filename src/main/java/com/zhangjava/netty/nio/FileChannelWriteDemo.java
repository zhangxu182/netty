package com.zhangjava.netty.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * FileChannel用法演示
 *
 * @author zhangxu
 * @date 2021/2/28 17:24
 */
public class FileChannelWriteDemo {
    public static void main(String[] args) {
        String str = "hello world";
        try {
            FileOutputStream outputStream = new FileOutputStream("1.txt");
            // 获取FileChannel
            FileChannel channel = outputStream.getChannel();

            // 创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 将数据写入到缓冲区
            buffer.put(str.getBytes(StandardCharsets.UTF_8));
            // 切换buffer为读模式
            buffer.flip();

            // 将缓冲区数据写入到通道
            channel.write(buffer);
            // 关闭流
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
