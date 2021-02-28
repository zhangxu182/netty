package com.zhangjava.netty.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * BIO模型，客户端
 *
 * @author zhangxu
 * @date 2021/2/27 17:21
 */
public class BioClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            OutputStream outputStream = socket.getOutputStream();
            while (true) {
                System.out.print("请输入：");
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();
                outputStream.write(s.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
