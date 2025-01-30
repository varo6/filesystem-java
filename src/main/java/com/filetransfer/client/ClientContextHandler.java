package com.filetransfer.client;

import com.filetransfer.common.CommandMessage;
import com.filetransfer.common.Context;
import com.filetransfer.common.ContextCommandHandler;
import com.filetransfer.common.ContextManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ClientContextHandler implements ContextCommandHandler {
    private ContextManager contextManager;
    private SimpleClient client;

    public ClientContextHandler(ContextManager manager){
        this.contextManager = manager;
    }
    public void setClient(SimpleClient client) {
        this.client = client;
    }
    @Override
    public boolean handleCommand(String[] command) throws Exception {
        CommandMessage commandMessage = null;
        switch (command[0]) {
            case "--close":
                /**
                 * Implementar la lógica para cerrar adecuandamente el cliente
                 * */
                System.out.println("Closing CLIENT context...");
                contextManager.changeContext(Context.SYSTEM);
                break; //Ejecuta el comando, es reconocido

            case "?":
            case "help":
                System.out.println("Available commands: " + getAvailableCommands());
                break;

            case "scp":
                if (command.length < 4) {
                    System.out.println("Uso: scp -u/-d <origen> <destino>");
                    return false;
                }

                if (command[1].equals("-u")) {
                    Path localPath = Paths.get("FileSystem/storage").resolve(command[2]);
                    System.out.println("Intentando acceder a: " + localPath.toAbsolutePath());
                    if (!Files.exists(localPath)) {
                        System.out.println("Error: El archivo local no existe: " + localPath);
                        return false;
                    }
                    try {
                        byte[] fileContent = Files.readAllBytes(localPath);
                        commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.FILE_UPLOAD)
                                .addArg("-u")
                                .addArg(command[2])  // nombre del archivo para el servidor
                                .addArg(command[3])  // destino en el servidor
                                .setPayload(fileContent)
                                .build();
                    } catch (IOException e) {
                        System.err.println("Error al leer el archivo: " + e.getMessage());
                        return false;
                    }
                } else if (command[1].equals("-d")) {
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.FILE_DOWNLOAD)
                            .addArg("-d")
                            .addArg(command[2])  // archivo en el servidor
                            .addArg(command[3])  // destino local
                            .build();
                }
                break;

            case "dir":
            case "ls":
                if (ClientUtils.validateLsArg(command)) {
                    if (command.length == 1) {
                        commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_LIST)
                                .build();
                        break;
                    } else {
                        commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_LIST)
                                .addArg(command[1])
                                .build();
                        break;
                    }
                }
                else { return false; }

            case "mkdir":
                if (ClientUtils.validateMkdirArg(command)) {
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_CREATE)
                              .addArg(command[1])
                              .build();

                    break;
                }
                else { return false; }

            case "pwd":
                if (ClientUtils.validatePwdArg(command)) {
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_LOCATION)
                            .build();
                    break;
                }
                else { return false; }

            case "cd":
                if (ClientUtils.validateCdArg(command)) {
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_OPEN)
                            .addArg(command[1])
                            .build();
                    break;
                }
                else { return false; }

            case "rm":
                if (ClientUtils.validateRmArg(command)) {
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.FILE_DELETE)
                            .addArg(command[1])
                            .build();
                    break;
                }
                else { return false; }

            case "rn":
                if (ClientUtils.validateRnArg(command)) {
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.FILE_RENAME)
                            .addArg(command[1])
                            .addArg(command[2])
                            .build();
                    break;
                }
                else { return false; }

            case "echo":
                if (ClientUtils.validadeEchoArg(command)) {
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.ECHO_FILE)
                            .addArg(command[1])
                            .build();
                    break;
                }
                else { return false; }

            case "ccheck":
                if (ClientUtils.validateCheckConArg(command)) {
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.CON_CHECK)
                            .addArg(command[1])
                            .build();
                    break;
                }
                else { return false; }

            default:
                return false;
        }
        if (commandMessage != null && client != null) {
            client.sendCommand(commandMessage);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAvailableCommands() {
        return Arrays.asList(
                "--close",
                "help",
                "?",
                "scp -u/-d <origen> <destino>",
                "ls/dir",
                "mkdir <directorio>",
                "pwd",
                "cd <ruta>",
                "rm <archivo>",
                "rn <actual> <nuevo>",
                "ccheck <parametro>"
        );
    }
}
