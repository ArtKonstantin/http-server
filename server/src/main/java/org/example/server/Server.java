package org.example.server;

import com.google.common.primitives.Bytes;
import lombok.Setter;
import org.example.server.exception.BadRequestException;
import org.example.server.exception.DeadlineExceedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Setter
public class Server {

    public static final byte[] CRLFCRLF = {'\r', '\n', '\r', '\n'};
    public static final byte[] CRLF = {'\r', '\n'};
    private int port = 9999;
    private int soTimeout = 30 * 1000;
    private int readTimeout = 60 * 1000;
    private int bufferSize = 4096;

    public void start() {
        // ServerSocket
        // Socket
        try (
                final ServerSocket serverSocket = new ServerSocket(port);
        ) {
            while (true) {
                // блокирующий вызов
                try {
                    final Socket socket = serverSocket.accept();
                    handleClient(socket);
                } catch (Exception e) {
                    // если произошла любая проблема с клиентом
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // если не удалось запустить сервер
            e.printStackTrace();
        }
    }

    private void handleClient(final Socket socket) throws IOException {

        try (
                socket;
                final OutputStream out = socket.getOutputStream();
                final InputStream in = socket.getInputStream();
        ) {
            socket.setSoTimeout(soTimeout);
            System.out.println(socket.getInetAddress());

            final Request request = readRequest(in);
            System.out.println("request = " + request);

            // Ctrl + Alt + L - форматирование
            final String response = "HTTP/1.1 200 OK\r\n" +
                    "Connection: close\r\n" +
                    "Content-Length: 2\r\n" +
                    "\r\n" +
                    "OK";

            out.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private Request readRequest(final InputStream in) throws IOException {
        final byte[] buffer = new byte[bufferSize];
        int offset = 0;
        int length = buffer.length;
        // deadline

        Instant deadline = Instant.now().plusMillis(readTimeout);
        // внутренний цикл чтения команды
        while (true) {
            if (Instant.now().isAfter(deadline)) {
                throw new DeadlineExceedException();
            }

            final int read = in.read(buffer, offset, length); // read - сколько байт было прочитано
            offset += read; // offset = offset + read;
            length = buffer.length - offset;

            final int headersEndIndex = Bytes.indexOf(buffer, CRLFCRLF);
            if (headersEndIndex != -1) {
                break;
            }

            if (read == -1) {
                throw new BadRequestException("CRLFCRLF not found, no more data");
            }

            if (length == 0) {
                throw new BadRequestException("CRLFCRLF not found");
            }
        }
        Request request = new Request();
        int requestLineEndIndex = Bytes.indexOf(buffer, CRLF);
        if (requestLineEndIndex == -1) {
            throw new BadRequestException("Request line not found");
        }
        String requestLine = new String(buffer, 0, requestLineEndIndex, StandardCharsets.UTF_8);
        System.out.println("requestLine = " + requestLine);
        parseRequestLine(requestLine);

        return request;
    }

    private void parseRequestLine(String requestLine) {
        String[] parts = requestLine.split(" ");
        System.out.println("method: " + parts[0]);
        System.out.println("path: " + parts[1]);
        System.out.println("version: " + parts[2]);
    }
}
