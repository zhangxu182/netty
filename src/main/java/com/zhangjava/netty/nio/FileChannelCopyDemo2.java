package com.zhangjava.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 文件拷贝
 *
 * @author zhangxu
 * @date 2021/2/28 18:29
 */
public class FileChannelCopyDemo2 {
    public static void main(String[] args) {
        try {
            FileInputStream inputStream = new FileInputStream("1.txt");
            FileOutputStream outputStream = new FileOutputStream("2.txt");

            FileChannel inputStreamChannel = inputStream.getChannel();
            FileChannel outputStreamChannel = outputStream.getChannel();

            // 文件拷贝
            inputStreamChannel.transferTo(0, inputStreamChannel.size(), outputStreamChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
