package com.filetransfer;

import com.filetransfer.common.ContextManager;
import com.filetransfer.common.ConsoleGUI;
import com.filetransfer.common.Const;

import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class FileSystem{
    boolean noGui = false;
    boolean client;
    boolean server;
    FileSystem instance;
    String address;
    int port;
    int maxConnections;
    private ContextManager contextManager = new ContextManager();

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public FileSystem(String[] args) throws Exception {
        initialize();
        loadConfig();
        parseArgs(args);
        run();
    }

    private void parseArgs(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case "--nogui":
                    noGui = true;
                    break;
                case "--client":
                    client = true;
                    break;
                case "--server":
                    server = true;
                    break;
                default:
                    System.out.println("Argumento no reconocido: " + arg);
                    break;
            }
        }
    }


    private void initialize() {
        File fileSystemDir = new File(Const.FILE_SYSTEM_DIRECTORY);
        System.out.println("File path: "+ Const.FILE_SYSTEM_DIRECTORY);
        if (!fileSystemDir.exists()) {
            boolean fileSystemDirCreated = fileSystemDir.mkdirs();
            if (fileSystemDirCreated) {
                System.out.println("'FileSystem' directory created");
            } else {
                System.err.println("Could not create 'FileSystem' directory.");
            }
        }
    }

    private void loadConfig() throws IOException {
        port = 5000;
        maxConnections = 10;
        address = "localhost";

        File configFile = new File(Const.CONFIG_FILE_PATH);
        if (!configFile.exists()) return;

        try (InputStream inputStream = new FileInputStream(configFile)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);

            if (data != null) {
                port = (Integer) data.getOrDefault("server-port", 5000);
                maxConnections = (Integer) data.getOrDefault("max-connections", 10);
                address = (String) data.getOrDefault("client-default-server-ip", "localhost");
            }
        }
    }


    private void run() throws Exception {
        if (!noGui) {
            startGUI();
        } else {
            if (client) contextManager.processCommand("--client");
            if (server) contextManager.processCommand("--server");
            startCommandLoop();
        }
    }

    private void startGUI() {
        SwingUtilities.invokeLater(() -> new ConsoleGUI(contextManager));
    }


    private void startCommandLoop() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                break;
            }
            try {
                contextManager.processCommand(input);
            } catch (Exception e) {
                System.err.println("Error procesando comando: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void initializeClient() throws Exception {
        System.out.println("Iniciando cliente en " + address + ":" + port);
    }

    private void initializeServer() throws Exception {
        System.out.println("Iniciando servidor en puerto " + port);
    }

    public ContextManager getContextManager() {
        return contextManager;
    }

    public void setContextManager(ContextManager contextManager) {
        this.contextManager = contextManager;
    }
}

