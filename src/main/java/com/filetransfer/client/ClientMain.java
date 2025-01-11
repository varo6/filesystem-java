package com.filetransfer.client;

import com.filetransfer.common.Context;
import com.filetransfer.common.ContextManager;
import java.io.IOException;

public class ClientMain {
    private String host;
    private int port;
    private SimpleClient client;
    private volatile boolean isConnected = false;
    private ClientContextHandler clientHandler;

    public ClientMain(String host, int port, ContextManager contextManager) {
        this.host = host;
        this.port = port;
        this.clientHandler = (ClientContextHandler) contextManager.getContextHandler(Context.CLIENT);
    }

    public void start() throws IOException {
        System.out.println("Conectando a host remoto " + host + ":" + port + "...");
        try {
            client = new SimpleClient(host, port);
            clientHandler.setClient(client); // Establecer el cliente en el handler
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
            client.stopClient();
        }
        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean sendMessage(String[] message) {
        if (client != null && isConnected) {
            client.sendMessage(message);
            return true;
        }
        System.err.println("Cliente no conectado");
        return false;
    }
}