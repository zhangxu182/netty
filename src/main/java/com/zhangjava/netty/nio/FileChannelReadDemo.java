package com.zhangjava.netty.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel读取文件内容
 *
 * @author zhangxu
 * @date 2021/2/28 17:40
 */
public class FileChannelReadDemo {
    public static void main(String[] args) {
        try {
            FileInputStream inputStream = new FileInputStream("1.txt");
            // 获取通道
            FileChannel channel = inputStream.getChannel();
            // 创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 将通道的数据读入缓冲区
            channel.read(buffer);
            // 切换读写模式
            buffer.flip();
            System.out.println(new String(buffer.array()));
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
