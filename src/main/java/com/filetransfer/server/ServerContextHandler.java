package com.filetransfer.server;

import com.filetransfer.common.ContextCommandHandler;

import java.util.Arrays;
import java.util.List;

public class ServerContextHandler implements ContextCommandHandler {
    @Override
    public boolean handleCommand(String command) throws Exception {
        switch(command) {
            case "?":
            case "--help":
            case "-h":
                getAvailableCommands();
            case "--exit":
                return true;
            default:
                System.out.println("Comando no válido en contexto SERVER");
                return false;
        }
    }

    @Override
    public List<String> getAvailableCommands() {
        return Arrays.asList("--exit");
    }
}
