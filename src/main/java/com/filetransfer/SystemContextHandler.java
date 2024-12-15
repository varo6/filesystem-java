package com.filetransfer;

import com.filetransfer.common.ContextCommandHandler;

import java.util.Arrays;
import java.util.List;

public class SystemContextHandler implements ContextCommandHandler {
    @Override
    public boolean handleCommand(String command) throws Exception {
        switch(command) {
            case "?":
            case "--help":
            case "-h":
                getAvailableCommands();
            case "--client":

                return true;
            case "--server":

                return true;
            default:
                System.out.println("Comando no válido en contexto SYSTEM");
                return false;
        }
    }

    @Override
    public List<String> getAvailableCommands() {
        return Arrays.asList("--client", "--server");
    }
}