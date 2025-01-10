package com.filetransfer;

import com.filetransfer.common.ContextManager;
import com.filetransfer.common.ConsoleGUI;
import com.filetransfer.common.Const;

import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileSystem {
    boolean noGui = false;
    boolean client;
    boolean server;
    private ContextManager contextManager = new ContextManager();

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
        if (!fileSystemDir.exists()) {
            System.out.println("FILE SYSTEM DIR EXISTS, CHECK: "+fileSystemDir.exists());
            boolean fileSystemDirCreated = fileSystemDir.mkdirs();
            if (fileSystemDirCreated) {
                System.out.println("'FileSystem' directory created");
            } else {
                System.err.println("Could not create 'FileSystem' directory.");
            }
        }

        File storageDir = new File(Const.STORAGE_DIRECTORY);
        if (!storageDir.exists()) {
            boolean storageDirCreated = storageDir.mkdirs();
            if (storageDirCreated) {
                System.out.println("'storage' directory created");
            } else {
                System.err.println("Could not create 'storage' directory.");
            }
        }

        File configFile = new File(Const.CONFIG_FILE_PATH);
        if (!configFile.exists()) {
            try {
                boolean configFileCreated = configFile.createNewFile();
                if (configFileCreated) {
                    System.out.println("config.yml file created");

                    try (FileWriter writer = new FileWriter(configFile)) {
                        Yaml yaml = new Yaml();
                        Map<String, Object> defaultData = new HashMap<>();
                        defaultData.put("server-port", 5000);
                        defaultData.put("max-connections", 10);
                        defaultData.put("client-default-server-ip", "localhost");

                        yaml.dump(defaultData, writer);
                    }
                } else {
                    System.err.println("Could not create 'config.yml' file.");
                }
            } catch (IOException e) {
                System.err.println("Error creating 'config.yml': " + e.getMessage());
            }
        }

    }

    private void loadConfig() throws IOException {
        contextManager.setPort(5000);
        contextManager.setMaxConnections(10);
        contextManager.setAddress("localhost");

        File configFile = new File(Const.CONFIG_FILE_PATH);
        if (!configFile.exists()) return;

        try (InputStream inputStream = new FileInputStream(configFile)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);

            if (data != null) {
                contextManager.setPort((Integer) data.getOrDefault("server-port", 5000));
                contextManager.setMaxConnections((Integer) data.getOrDefault("max-connections", 10));
                contextManager.setAddress((String) data.getOrDefault("client-default-server-ip", "localhost"));
            }
        }
    }


    private void run() throws Exception {
        if (!noGui) {
            startGUI();
        } else {
            if (client) contextManager.processCommand(new String[]{"--client"});
            if (server) contextManager.processCommand(new String[]{"--server"});
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
            String[] command = input.split(" ");
            if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                break;
            }
            try {
                contextManager.processCommand(command);
            } catch (Exception e) {
                System.err.println("Error while processing command: " + e.getMessage());
            }
        }
        scanner.close();
    }

    public ContextManager getContextManager() {
        return contextManager;
    }

    public void setContextManager(ContextManager contextManager) {
        this.contextManager = contextManager;
    }
}

