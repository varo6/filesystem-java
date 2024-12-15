package com.filetransfer;

import com.filetransfer.client.ClientMain;
import com.filetransfer.server.ServerMain;
import com.filetransfer.common.Const;
import com.filetransfer.common.LoadConfig;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class FileSystem {
    FileSystem instance;
    public FileSystem() {
        instance = this;
    }

    public void initialize() {
        initializeFileSystem();
    }

    private void initializeFileSystem() {
        File fileSystemDir = new File(Const.FILE_SYSTEM_DIRECTORY);
        if (!fileSystemDir.exists()) {
            boolean fileSystemDirCreated = fileSystemDir.mkdirs();
            if (fileSystemDirCreated) {
                System.out.println("Directorio 'FileSystem' creado en: " + Const.FILE_SYSTEM_DIRECTORY);
            } else {
                System.err.println("No se pudo crear el directorio 'FileSystem'.");
            }
        }

        File storageDir = new File(Const.STORAGE_DIRECTORY);
        if (!storageDir.exists()) {
            boolean storageDirCreated = storageDir.mkdirs();
            if (storageDirCreated) {
                System.out.println("Directorio 'storage' creado en: " + Const.STORAGE_DIRECTORY);
            } else {
                System.err.println("No se pudo crear el directorio 'storage'.");
            }
        }

        File configFile = new File(Const.CONFIG_FILE_PATH);
        if (!configFile.exists()) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml");
                 OutputStream out = new FileOutputStream(configFile)) {

                if (in == null) {
                    System.err.println("No se encontró el archivo 'config.yml' en los recursos.");
                    return;
                }

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

                System.out.println("Archivo 'config.yml' copiado a: " + Const.CONFIG_FILE_PATH);
            } catch (IOException e) {
                System.err.println("Error al copiar el archivo 'config.yml': " + e.getMessage());
            }
        } else {
            System.out.println("Archivo 'config.yml' ya existe en: " + Const.CONFIG_FILE_PATH);
        }
    }

    public Map<String, Object> loadConfig() throws IOException {
        return LoadConfig.loadConfig(Const.CONFIG_FILE_PATH);
    }


    public void runConsoleMode(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String[] input;


        Map<String, Object> config = loadConfig();
        if (config == null) {
            System.err.println("Error al cargar la configuración.");
            return;
        }
        int serverPort = (int) config.get("server-port");
        int maxConnections = (int) config.get("max-connections");


        System.out.println("Configuración cargada:");
        System.out.println("Server Port: " + serverPort);
        System.out.println("Max Connections: " + maxConnections);

        if (args.length == 0) {
            System.out.println("Por favor, proporciona un argumento: --client o --server o --exit");
            System.out.print("Introduce la opción: ");
            String userInput = scanner.nextLine();
            input = userInput.split(" ");
        } else {
            input = args;
        }

        if (input.length == 0) {
            System.out.println("No se proporcionaron argumentos válidos.");
            return;
        }

        /**
         * Estos son los posibles inputs tras ejecutar el programa
         * */
        switch (input[0]) {
            case "--client":
            case "-c":
                System.out.println("Iniciando cliente...");
                if (input.length == 3) {
                    String address = input[1];
                    try {
                        int port = Integer.parseInt(input[2]);
                        ClientMain c = new ClientMain(address, port);
                        c.start();
                    } catch (NumberFormatException e) {
                        System.err.println("Error: El puerto debe ser un número entero válido.");
                    }
                } else {
                    ClientMain c = new ClientMain();
                    c.start();
                }
                break;

            case "--server":
            case "-s":
                System.out.println("Iniciando servidor...");
                if (input.length == 2) {
                    try {
                        int port = Integer.parseInt(input[1]);
                        ServerMain s = new ServerMain(port);
                        s.start();
                    } catch (NumberFormatException e) {
                        System.err.println("Error: El puerto debe ser un número entero válido.");
                    }
                } else {
                    ServerMain s = new ServerMain();
                    s.start();
                }
                break;

            case "--exit":
            case "-e":
                System.out.println("Cerrando programa.");
                break;

            default:
                System.err.println("Argumento no reconocido. Usa --client, --server o --exit.");
                break;
        }

        scanner.close();
    }


    public void startServer(int port) throws IOException {
        ServerMain server = new ServerMain(port);
        server.start();
    }


    public void startClient(String address, int port) throws IOException {
        ClientMain client = new ClientMain(address, port);
        client.start();
    }
}
