package com.filetransfer.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SimpleServer implements Runnable {
    private final Socket socket;
    private volatile boolean running = true;
    private PrintWriter out;
    private BufferedReader in;

    public SimpleServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            out.println("Welcome to the server!");

            String clientMessage;
            while (running && (clientMessage = in.readLine()) != null) {
                System.out.println("Recibido del cliente: " + clientMessage);

                if (clientMessage.equalsIgnoreCase("quit")) {
                    out.println("Goodbye!");
                    break;
                }

                out.println("Echo: " + clientMessage);
            }
        } catch (IOException e) {
            if (running && !socket.isClosed()) {
                System.err.println("Error en la comunicación: " + e.getMessage());
            }
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        running = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (!socket.isClosed()) socket.close();
            System.out.println("Cliente desconectado");
        } catch (IOException e) {
            System.err.println("Error en la desconexión: " + e.getMessage());
        }
    }
}