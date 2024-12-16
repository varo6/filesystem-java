package com.filetransfer.client;

import com.filetransfer.common.ContextManager;

import java.io.IOException;

public class ClientMain extends ContextManager {
    private String host;
    private int port;

    public ClientMain(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException {
        System.out.println("Conectando a host remoto " + host + ":" + port + "...");
        SimpleClient c = new SimpleClient(host, port);
        c.runClient();
    }
}
