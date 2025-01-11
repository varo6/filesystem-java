package com.filetransfer.client;

import com.filetransfer.common.CommandMessage;
import com.filetransfer.common.Context;
import com.filetransfer.common.ContextCommandHandler;
import com.filetransfer.common.ContextManager;

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
                if (ClientUtils.validateScpArg(command)) {
                    if (command[1].equals("-u")){
                        System.out.println("tu comando es valido: (subida)" + command[0] + " " + command[1] + " " + command[2] + " " + command[3]);
                        commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.FILE_UPLOAD)
                                .addArg(command[1])
                                .addArg(command[3])
                                .setPayload("Aqui van todos los bytes del archivo a transmitir, la informacion util".getBytes())
                                .build();
                        break;
                    } else if (command[1].equals("-d")){
                        System.out.println("tu comando es valido: (descarga)" + command[0] + " " + command[1] + " " + command[2] + " " + command[3]);
                        commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.FILE_DOWNLOAD)
                                .addArg(command[1])
                                .addArg(command[3])
                                .build();
                        break;
                    }
                }
                else { return false; }

            case "dir":
            case "ls":
                if (ClientUtils.validateLsArg(command)) {
                    System.out.println("tu comando es valido: " + command[0] + " " + command[1]);
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_LIST)
                            .addArg(command[1])
                            .build();
                    break;
                }
                else { return false; }

            case "mkdir":
                if (ClientUtils.validateMkdirArg(command)) {
                    System.out.println("tu comando es valido: " + command[0] + " " + command[1]);
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_CREATE)
                              .addArg(command[1])
                              .build();

                    break;
                }
                else { return false; }

            case "pwd":
                if (ClientUtils.validatePwdArg(command)) {
                    System.out.println("tu comando es valido: " + command[0]);
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_LOCATION)
                            .build();
                    break;
                }
                else { return false; }

            case "cd":
                if (ClientUtils.validateCdArg(command)) {
                    System.out.println("tu comando es valido: " + command[0] + " " + command[1]);
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_OPEN)
                            .addArg(command[1])
                            .build();
                    break;
                }
                else { return false; }

            case "rm":
                if (ClientUtils.validateRmArg(command)) {
                    System.out.println("tu comando es valido: " + command[0] + " " + command[1]);
                    commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.FILE_DELETE)
                            .addArg(command[1])
                            .build();
                    break;
                }
                else { return false; }

            default:
                return false; //Comando no reconocido
        }
        if (commandMessage != null && client != null) {
            client.sendCommand(commandMessage);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAvailableCommands() {
        return Arrays.asList("--close", "--help", "?");
    }
}
