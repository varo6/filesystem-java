package com.filetransfer.server;

import com.filetransfer.common.CommandMessage;
import com.filetransfer.common.Header;
import com.filetransfer.common.Const;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SimpleServer implements Runnable {
    private final Socket socket;
    private volatile boolean running = true;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectInputStream ois;

    public SimpleServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            ois = new ObjectInputStream(socket.getInputStream());
            out.println("Welcome to the server!");

            while (running) {

                // Leer el header porque todos los mensajes tendrán header
                Header h= (Header) ois.readObject();
                switch(h.getType()) {
                    case Const.TYPE_COMMAND:
                        CommandMessage cm = (CommandMessage) h;
                        //handle command

                    case Const.TYPE_TEXT:
                        //handle text

                    case Const.TYPE_CLOSE:
                        out.println("Goodbye!");
                        cleanup();
                        break;

                    default:
                        System.out.println("Unknown message type");
                }

            }
        } catch (IOException e) {
            if (running && !socket.isClosed()) {
                System.err.println("Error en la comunicación: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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