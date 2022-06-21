package org.example.app;

import org.example.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.setPort(7777);
        server.start();
    }
}