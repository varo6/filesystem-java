package com.filetransfer;

import com.filetransfer.client.ClientMain;
import com.filetransfer.common.Context;
import com.filetransfer.common.ContextCommandHandler;
import com.filetransfer.common.ContextManager;
import com.filetransfer.server.ServerMain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SystemContextHandler implements ContextCommandHandler {

    private ContextManager manager;

    public SystemContextHandler(ContextManager contextManager) {
        this.manager = contextManager;
    }

    @Override
    public boolean handleCommand(String[] command) throws Exception {
        switch (command[0]) {
            case "?":
            case "--help":
            case "-h":
                getAvailableCommands();
            case "--client":

                manager.stopActiveThread();

                Thread clientThread = new Thread(() -> {
                    if(command.length == 3){
                        ClientMain client = new ClientMain(command[1], Integer.parseInt(command[2]),manager);
                        try {
                            client.start();
                        } catch (IOException e) {
                            System.err.println("Error in client: " + e.getMessage());
                        }
                    }
                    else{
                        ClientMain client = new ClientMain(manager.getAddress(), manager.getPort(),manager);
                        try {
                            client.start();
                        } catch (IOException e) {
                            System.err.println("Error in client: " + e.getMessage());
                        }
                    }

                });
                manager.setActiveThread(clientThread);
                manager.getActiveThread().start();
                manager.changeContext(Context.CLIENT);
                return true;

            case "--server":
                manager.stopActiveThread();


                 Thread serverThread = new Thread(() -> {
                     if(command.length == 2){
                         ServerMain server = new ServerMain(Integer.parseInt(command[1]));
                         try {
                             server.start();
                         } catch (IOException e) {
                             System.err.println("Error in server: " + e.getMessage());
                         }
                     }
                     else {
                         ServerMain server = new ServerMain(manager.getPort());
                         try {
                             server.start();
                         } catch (IOException e) {
                             System.err.println("Error in server: " + e.getMessage());
                         }
                     }

                });
                manager.setActiveThread(serverThread);
                manager.getActiveThread().start();

                manager.changeContext(Context.SERVER);
                return true;

            case "--exit":
                System.out.println("Closing program.");
                manager.stopActiveThread();
                System.exit(0);
                return true;

            default:
                return false;
        }
    }

    @Override
    public List<String> getAvailableCommands() {
        return Arrays.asList("--client [address] [port]", "--server [port]", "--exit");
    }
}