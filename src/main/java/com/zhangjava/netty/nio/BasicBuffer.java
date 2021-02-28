package com.zhangjava.netty.nio;

import java.nio.IntBuffer;

/**
 * Buffer基本操作
 *
 * @author zhangxu
 * @date 2021/2/28 10:31
 */
public class BasicBuffer {
    public static void main(String[] args) {
        // 创建一个int类型buffer，大小为5
        IntBuffer intBuffer = IntBuffer.allocate(5);
        // 写入数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 读写切换
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
