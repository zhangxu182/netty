package com.zhangjava.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件拷贝演示
 *
 * @author zhangxu
 * @date 2021/2/28 17:53
 */
public class FileChannelCopyDemo {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("1.txt");
            FileChannel channel = fileInputStream.getChannel();

            FileOutputStream outputStream = new FileOutputStream("2.txt");
            FileChannel fileChannel = outputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 循环读取，写入
            while (true) {
                // 清除缓冲区
                buffer.clear();
                // 将数据读入到缓冲区
                int read = channel.read(buffer);
                // -1表示文件读取完毕
                if (read == -1) {
                    break;
                }

                // 切换模式
                buffer.flip();
                // 数据写入到缓冲区
                fileChannel.write(buffer);
            }

            // 关闭流
            fileInputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
