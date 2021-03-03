package com.zhangjava.netty.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer可让文件直接在内存修改，操作系统不需要拷贝一次
 *
 * @author zhangxu
 * @date 2021/3/1 21:29
 */
public class MappedByteBufferTest {
    public static void main(String[] args) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 10);
            mappedByteBuffer.put(2, (byte) 'A');

            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
