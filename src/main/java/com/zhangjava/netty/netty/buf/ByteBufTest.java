package com.zhangjava.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author zhangxu
 * @date 2021/3/11 20:38
 */
public class ByteBufTest {
    public static void main(String[] args) {
        /*
            创建一个ByteBuf
            1.创建对象，该对象包含一个数组arr，是一个byte[]
            2.在netty的buffer中，不需要使用flip进行反转，底层维护了readerIndex，writerIndex和capacity
              将buffer分成三个区域
              0 -- readerIndex 已读区域
              readerIndex -- writerIndex 可读区域
              writerIndex -- capacity 可写区域
         */
        ByteBuf byteBuf = Unpooled.buffer(5);
        for (int i = 0; i < byteBuf.capacity(); i++) {
            byteBuf.writeByte(i);
        }

        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.readByte());
        }
    }
}
