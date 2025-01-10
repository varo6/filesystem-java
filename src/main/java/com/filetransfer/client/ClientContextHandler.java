package com.filetransfer.client;

import com.filetransfer.common.CommandMessage;
import com.filetransfer.common.Context;
import com.filetransfer.common.ContextCommandHandler;
import com.filetransfer.common.ContextManager;

import java.util.Arrays;
import java.util.List;

public class ClientContextHandler implements ContextCommandHandler {
    private ContextManager contextManager;

    public ClientContextHandler(ContextManager manager){
        this.contextManager = manager;
    }
    @Override
    public boolean handleCommand(String[] command) throws Exception {
        switch (command[0]) {
            case "--close":
                /**
                 * Implementar la lógica para cerrar adecuandamente el cliente
                 * */
                System.out.println("Closing CLIENT context...");
                contextManager.changeContext(Context.SYSTEM);
                return true; //Ejecuta el comando, es reconocido

            case "?":
            case "help":
                System.out.println("Available commands: " + getAvailableCommands());
                return true;

            case "scp":
                if (ClientUtils.validateScpArg(command)) {
                    if (command[1].equals("-u")){
                        System.out.println("tu comando es valido: (subida)" + command[0] + " " + command[1] + " " + command[2] + " " + command[3]);
                        CommandMessage uploadCommand = new CommandMessage.Builder(CommandMessage.CommandType.FILE_UPLOAD)
                                .addArg(command[1])
                                .addArg(command[3])
                                .setPayload("Aqui van todos los bytes del archivo a transmitir, la informacion util".getBytes())
                                .build();
                        return true;
                    } else if (command[1].equals("-d")){
                        System.out.println("tu comando es valido: (descarga)" + command[0] + " " + command[1] + " " + command[2] + " " + command[3]);
                        CommandMessage downloadCommand = new CommandMessage.Builder(CommandMessage.CommandType.FILE_DOWNLOAD)
                                .addArg(command[1])
                                .addArg(command[3])
                                .build();
                        return true;
                    }
                }
                else { return false; }

            case "dir":
            case "ls":
                if (ClientUtils.validateLsArg(command)) {
                    System.out.println("tu comando es valido: " + command[0] + " " + command[1]);
                    CommandMessage lsCommand = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_LIST)
                            .addArg(command[1])
                            .build();
                    return true;
                }
                else { return false; }

            case "mkdir":
                if (ClientUtils.validateMkdirArg(command)) {
                    System.out.println("tu comando es valido: " + command[0] + " " + command[1]);
                    CommandMessage mkdirCommand = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_CREATE)
                              .addArg(command[1])
                              .build();
                    return true;
                }
                else { return false; }

            case "pwd":
                if (ClientUtils.validatePwdArg(command)) {
                    System.out.println("tu comando es valido: " + command[0]);
                    CommandMessage pwdCommand = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_LOCATION)
                            .build();
                    return true;
                }
                else { return false; }

            case "cd":
                if (ClientUtils.validateCdArg(command)) {
                    System.out.println("tu comando es valido: " + command[0] + " " + command[1]);
                    CommandMessage cdCommand = new CommandMessage.Builder(CommandMessage.CommandType.DIRECTORY_OPEN)
                            .addArg(command[1])
                            .build();
                    return true;
                }
                else { return false; }

            case "rm":
                if (ClientUtils.validateRmArg(command)) {
                    System.out.println("tu comando es valido: " + command[0] + " " + command[1]);
                    CommandMessage rmCommand = new CommandMessage.Builder(CommandMessage.CommandType.FILE_DELETE)
                            .addArg(command[1])
                            .build();
                    return true;
                }
                else { return false; }

            default:
                return false; //Comando no reconocido
        }
    }

    @Override
    public List<String> getAvailableCommands() {
        return Arrays.asList("--close", "--help", "?");
    }
}
