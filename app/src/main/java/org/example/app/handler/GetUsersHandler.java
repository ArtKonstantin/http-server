package org.example.app.handler;

import org.example.server.Handler;
import org.example.server.Request;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GetUsersHandler implements Handler {

    @Override
    public void handle(Request request, OutputStream responceStream) throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n" +
                "Connection: close\r\n" +
                "Content-Length: 2\r\n" +
                "\r\n" +
                "OK";

        responceStream.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
