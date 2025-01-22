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
            ensureDirectoryStructure();
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
            sendMessage("Welcome to the server!");
            Path path = Paths.get("./FileSystem/storage");

            while (running) {
                Object received = ois.readObject();
                if (received instanceof CommandMessage) {
                    handleCommand((CommandMessage) received, path);
                }
            }
        } catch (Exception e) {
            if (running && !socket.isClosed()) {
                System.err.println("Error en la comunicación: " + e.getMessage());
            }
        } finally {
            cleanup();
        }
    }


    private void handleCommand(CommandMessage cm, Path path) {
        try {
            if (cm.getCommandType() == CommandMessage.CommandType.FILE_DOWNLOAD) {
                handleFileDownload(cm, path);
            } else {
                String response = server.processCommand(cm, path);
                sendMessage(response);
            }
        } catch (Exception e) {
            System.err.println("Error handling command: " + e.getMessage());
        }
    }

    private void handleFileDownload(CommandMessage cm, Path path) {
        try {
            Path serverFilePath = path.resolve(cm.getArgs().get(1));
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
                sendMessage("Archivo enviado exitosamente");
            } else {
                sendMessage("Archivo no encontrado: " + serverFilePath);
            }
        } catch (IOException e) {
            System.err.println("Error en download: " + e.getMessage());
        }
    }

    private void sendMessage(String message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    private void ensureDirectoryStructure() {
        Path storagePath = Paths.get("FileSystem/storage");
        try {
            Files.createDirectories(storagePath);
            System.out.println("Storage directory: " + storagePath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cleanup() {
        running = false;
        try {
            if (in != null) in.close();
            if (!socket.isClosed()) socket.close();
            System.out.println("Cliente desconectado");
        } catch (IOException e) {
            System.err.println("Error en la desconexión: " + e.getMessage());
        }
    }
}