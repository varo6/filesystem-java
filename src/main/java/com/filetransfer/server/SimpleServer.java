package com.filetransfer.server;

import com.filetransfer.common.CommandMessage;
import com.filetransfer.common.Header;
import com.filetransfer.common.Const;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class SimpleServer implements Runnable {
    private final Socket socket;
    private volatile boolean running = true;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    ServerCommandProcess server = new ServerCommandProcess();

    public SimpleServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            out.println("Welcome to the server!");
            Path path = Paths.get("./FileSystem/storage");

            while (running) {
                Object creceived = ois.readObject();
                // Leer el header porque todos los mensajes tendrán header
                Header h= (Header) creceived;
                switch(h.getType()) {
                    case Const.TYPE_COMMAND:
                        CommandMessage cm = (CommandMessage) h;
                        if (cm.getCommandType() == CommandMessage.CommandType.FILE_DOWNLOAD) {
                            // Buscamos el archivo en el storage del servidor
                            Path serverFilePath = Paths.get("FileSystem/storage", cm.getArgs().get(1));
                            if (Files.exists(serverFilePath)) {
                                byte[] fileContent = Files.readAllBytes(serverFilePath);
                                CommandMessage response = new CommandMessage.Builder(CommandMessage.CommandType.FILE_DOWNLOAD)
                                        .addArg("-d")
                                        .addArg(cm.getArgs().get(1))
                                        .addArg(cm.getArgs().get(2))
                                        .setPayload(fileContent)
                                        .build();
                                oos.writeObject(response);
                                oos.flush();
                                out.println("Archivo enviado exitosamente");
                            } else {
                                out.println("Archivo no encontrado en el servidor: " + serverFilePath);
                            }
                        } else {
                            String response = server.processCommand(cm, path);
                            out.println(response);
                        }
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