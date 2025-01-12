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
                        commandMessage = new CommandMessage.Builder(CommandMessage.CommandType.FILE_UPLOAD)
                                .addArg(command[1])
                                .addArg(command[3])
                                .setPayload("Aqui van todos los bytes del archivo a transmitir, la informacion util".getBytes())
                                .build();
                        break;
                    } else if (command[1].equals("-d")){
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
        return Arrays.asList("--close", "--help", "?");
    }
}
