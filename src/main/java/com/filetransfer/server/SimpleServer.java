package com.filetransfer.server;

import com.filetransfer.common.CommandMessage;
import com.filetransfer.common.Header;
import com.filetransfer.common.Const;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Path;

public class SimpleServer implements Runnable {
    private final Socket socket;
    private volatile boolean running = true;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectInputStream ois;
    ServerCommandProcess server = new ServerCommandProcess();

    public SimpleServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            ois = new ObjectInputStream(socket.getInputStream());
            out.println("Welcome to the server!");
            Path path = Paths.get("./FileSystem/storage");

            while (running) {

                // Leer el header porque todos los mensajes tendrán header
                Header h= (Header) ois.readObject();
                switch(h.getType()) {
                    case Const.TYPE_COMMAND:
                        CommandMessage cm = (CommandMessage) h;
                        /**
                         * Comprobar si funciona, es una especie de echo en el servidor de lo que solicita el cliente
                         * */
                        CommandMessage.CommandType ct = cm.getCommandType();
                        String mainCommand = cm.getCommandString(ct);
                        String argsCommand= String.valueOf(cm.getArgs());
                        System.out.println(mainCommand + argsCommand);
                        String response = server.processCommand(cm, path);
                        out.println(response);
                        break;

                    case Const.TYPE_TEXT:
                        //handle text
                        break;

                    case Const.TYPE_CLOSE:
                        socket.close();
                        out.println("Goodbye!");
                        cleanup();
                        break;

                    default:
                        System.out.println("Unknown message type");
                        break;
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