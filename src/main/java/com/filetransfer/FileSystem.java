package com.filetransfer;

import com.filetransfer.common.ContextManager;
import com.filetransfer.common.ConsoleGUI;
import com.filetransfer.common.Const;

import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class FileSystem extends ContextManager {
    boolean noGui = false;
    boolean client;
    boolean server;
    FileSystem instance;
    String address;
    int port;
    int maxConnections;

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
        super();
        instance = this;

        System.out.println("Inicializamos carpetas");
        initialize();
        System.out.println("Cargamos la configuración");
        loadConfig();

        noGui = false;
        address = "localhost";
        port = 5000;

        if (args.length==0){
            Object[] options = {"Cliente", "Servidor"};
            int n = JOptionPane.showOptionDialog(null,
                    "Seleccione modo de inicio",
                    "Iniciar Aplicación",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch(n) {
                case 0: // Cliente
                    client = true;
                    break;
                case 1: // Servidor
                    server = true;
                    break;
            }
        }
        else{
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--nogui":
                        noGui = true;
                        break;
                    case "--client":
                    case "-c":
                        if (i + 2 < args.length) {
                            address = args[i + 1];
                            port = Integer.parseInt(args[i + 2]);
                            i += 2;
                        }
                        break;
                    case "--server":
                    case "-s":
                        if (i + 1 < args.length) {
                            port = Integer.parseInt(args[i + 1]);
                            i++;
                        }
                        break;
                    default:
                        System.out.println("Arg not recognized: " + args[i]);
                }
            }
        }
        run();
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

    public void loadConfig() throws IOException {
        try (InputStream inputStream = new FileInputStream(Const.CONFIG_FILE_PATH)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);

            if (data == null) {
                System.err.println("Error loading config.");
                maxConnections = 10;
                return;
            }

            port = (Integer) data.get("server-port");
            maxConnections = (Integer) data.get("max-connections");
            address = (String) data.get("client-default-server-ip");
        }
    }

    public void run() throws Exception {
        if (!noGui ||(!client&&!server)) {
            // Modo GUI
            SwingUtilities.invokeLater(() -> {
                try {
                    new ConsoleGUI(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            if (client) {
                new Thread(() -> {
                    try {
                        initializeClient();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            if (server) {
                // Iniciar servidor en hilo separado
                new Thread(() -> {
                    try {
                        initializeServer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            if (noGui) {
                Scanner scanner = new Scanner(System.in);

                while (true) {
                    System.out.print("> ");

                    try {
                        String input = scanner.nextLine().trim();
                        if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                            break;
                        }
                        processCommand(input);

                    } catch (Exception e) {
                        System.err.println("Error procesando comando: " + e.getMessage());
                    }
                }
                scanner.close();
            }
        }
    }

    private void initializeClient() throws Exception {
        System.out.println("Iniciando cliente en " + address + ":" + port);
    }

    private void initializeServer() throws Exception {
        System.out.println("Iniciando servidor en puerto " + port);
    }
}

