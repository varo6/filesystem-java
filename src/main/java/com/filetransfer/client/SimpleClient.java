package com.filetransfer.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SimpleClient {
    private final int port;
    private final String host;
    private volatile boolean running = true;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner;

    private final Object connectionLock = new Object();
    private volatile ConnectionState state = ConnectionState.DISCONNECTED;

    enum ConnectionState {
        CONNECTED, DISCONNECTED, CONNECTING
    }

    public boolean isConnected() {
        return state == ConnectionState.CONNECTED;
    }

    private void setState(ConnectionState newState) {
        synchronized (connectionLock) {
            System.out.println("State transition: " + state + " -> " + newState);
            this.state = newState;
        }
    }

    public SimpleClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void runClient() throws IOException {
        try {
            setState(ConnectionState.CONNECTING);
            socket = new Socket(host, port);
            setState(ConnectionState.CONNECTED);
            System.out.println("Connecting to server with = " + socket);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            // Thread para recibir mensajes del servidor
            Thread receiverThread = new Thread(this::receiveMessages);
            receiverThread.setDaemon(true);
            receiverThread.start();

            // Mantener la conexión viva
            while (running) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        } finally {
            cleanup();
            setState(ConnectionState.DISCONNECTED);
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while (running && (message = in.readLine()) != null) {
                System.out.println("Server: " + message);
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Error receiving message: " + e.getMessage());
            }
        } finally {
            setState(ConnectionState.DISCONNECTED);
        }
    }

    private void cleanup() {
        running = false;
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (scanner != null) {
                scanner.close();
            }
        } catch (IOException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        if (state != ConnectionState.CONNECTED) {
            System.err.println("Cannot send message. Client is not connected.");
            return;
        }
        if (out != null && running) {
            out.println(message);
        }
    }

    public void stopClient() {
        running = false;
        cleanup();
        setState(ConnectionState.DISCONNECTED);
        System.out.println("Client stopped.");
    }
}
