package com.filetransfer.client;

import com.filetransfer.common.ContextManager;
import java.io.IOException;

public class ClientMain extends ContextManager {
    private String host;
    private int port;
    private SimpleClient client;
    private volatile boolean isConnected = false;

    public ClientMain(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException {
        System.out.println("Conectando a host remoto " + host + ":" + port + "...");
        try {
            client = new SimpleClient(host, port);
            isConnected = true;
            client.runClient();
        } catch (IOException e) {
            isConnected = false;
            System.err.println("Error starting client: " + e.getMessage());
            throw e;
        }
    }

    public void stop() {
        if (client != null) {
            client.sendMessage("quit");
        }
    }

    public boolean sendMessage(String message) {
        if (client != null && isConnected) {
            client.sendMessage(message);
            return true;
        }
        System.err.println("Cliente no conectado");
        return false;
    }
}