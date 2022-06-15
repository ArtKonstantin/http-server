package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        OutputStream out = socket.getOutputStream();
                        InputStream in = socket.getInputStream();
                ) {
                    System.out.println(socket.getInetAddress());
                    out.write("Enter command\n".getBytes(StandardCharsets.UTF_8));

                    byte[] buffer = new byte[4096];
                    int offset = 0;
                    int length = buffer.length;
                    while (true) {
                        int read = in.read(buffer, offset, length);
                        offset += read;
                        length = buffer.length - offset;

                        if (read == 0 || length == 0) {
                            break;
                        }
                    }
                    String message = new String(buffer, 0, buffer.length - length, StandardCharsets.UTF_8).trim();
                    System.out.println("message = " + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
