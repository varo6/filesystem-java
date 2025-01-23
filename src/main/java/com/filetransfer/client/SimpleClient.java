package com.filetransfer.client;

import com.filetransfer.common.CommandMessage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class SimpleClient {
    private final int port;
    private final String host;
    private volatile boolean running = true;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
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
            ensureDirectoryStructure();
            setState(ConnectionState.CONNECTING);
            socket = new Socket(host, port);
            setState(ConnectionState.CONNECTED);
            System.out.println("Connecting to server with = " + socket);

            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            Thread receiverThread = new Thread(this::receiveMessages);
            receiverThread.setDaemon(true);
            receiverThread.start();

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

    private void ensureDirectoryStructure() {
        Path storagePath = Paths.get("FileSystem/storage");
        try {
            Files.createDirectories(storagePath);
            System.out.println("Storage directory: " + storagePath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        try {
            while (running) {
                Object received = ois.readObject();
                if (received instanceof CommandMessage) {
                    CommandMessage response = (CommandMessage) received;
                    handleCommandResponse(response);
                } else if (received instanceof String) {
                    System.out.println("Server: " + received);
                }
            }
        } catch (Exception e) {
            if (running) {
                System.err.println("Error receiving message: " + e.getMessage());
            }
        }
    }

    private void handleCommandResponse(CommandMessage response) {
        try {
            if (response.getCommandType() == CommandMessage.CommandType.FILE_DOWNLOAD) {
                String localFileName = response.getArgs().get(2);
                Path downloadPath = Paths.get("FileSystem/storage", localFileName);
                Files.createDirectories(downloadPath.getParent());
                Files.write(downloadPath, response.getPayload());
                System.out.println("Archivo descargado exitosamente en: " + downloadPath);
            }
        } catch (IOException e) {
            System.err.println("Error handling response: " + e.getMessage());
        }
    }


    public void sendMessage(String[] message) {
        if (state != ConnectionState.CONNECTED) {
            System.err.println("Cannot send message. Client is not connected.");
            return;
        }
        if (out != null && running) {
            try{
                out.println(message.toString());

            } catch (Exception e) {
                System.err.println("Error while processing command: " + e.getMessage());
            }
        }
    }

    public void sendCommand(CommandMessage cm) {
        if (state != ConnectionState.CONNECTED) {
            System.err.println("Cannot send command. Client is not connected.");
            return;
        }
        if (oos != null && running) {
            try {
                if (cm.getCommandType() == CommandMessage.CommandType.FILE_UPLOAD) {
                    String localPath = cm.getArgs().get(1); // ruta local
                    Path path = Paths.get("./FileSystem/storage").resolve(localPath);
                    if (!Files.exists(path)) {
                        System.err.println("Error: El archivo local no existe: " + localPath);
                        return;
                    }
                    // Actualizar el payload con el contenido del archivo
                    byte[] fileContent = Files.readAllBytes(path);
                    cm = new CommandMessage.Builder(cm.getCommandType())
                            .addArg(cm.getArgs().get(0))
                            .addArg(cm.getArgs().get(1))
                            .addArg(cm.getArgs().get(2))
                            .setPayload(fileContent)
                            .build();
                }
                oos.writeObject(cm);
                oos.flush();
            } catch (Exception e) {
                System.err.println("Error while processing command: " + e.getMessage());
            }
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

public void stopClient() {
        running = false;
        cleanup();
        setState(ConnectionState.DISCONNECTED);
        System.out.println("Client stopped.");
    }
}
