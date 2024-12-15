package com.filetransfer.client;

import com.filetransfer.common.ContextCommandHandler;

import java.util.Arrays;
import java.util.List;

public class ClientContextHandler implements ContextCommandHandler {
    @Override
    public boolean handleCommand(String command) throws Exception {
        switch(command) {
            case "?":
            case "--help":
            case "-h":
                getAvailableCommands();
            case "upload":
                return true;
            case "download":
                return true;
            case "--exit":
                return true;
            default:
                System.out.println("Comando no válido en contexto CLIENT");
                return false;
        }
    }

    @Override
    public List<String> getAvailableCommands() {
        return Arrays.asList("upload", "download", "--exit");
    }
}