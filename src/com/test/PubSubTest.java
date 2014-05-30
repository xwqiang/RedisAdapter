package com.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PubSubTest {
	public static void main(String[] args) {
		String cmd = "set kk bb" + "\r\n";
		try {
			Socket socket = new Socket("192.168.5.82", 6379);

			InputStream in = socket.getInputStream();

			OutputStream out = socket.getOutputStream();

			out.write(cmd.getBytes());
			// 发送订阅命令

			byte[] buffer = new byte[1024];

			while (true) {

				int readCount = in.read(buffer);

				System.out.write(buffer, 0, readCount);

				System.out.println("--------------------------------------");

			}
		} catch (Exception e) {
		}

	}
}
