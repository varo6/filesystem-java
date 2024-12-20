package com.filetransfer.client;

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
    public boolean handleCommand(String command) throws Exception {
        switch (command) {
            case "--close":
                /**
                 * Implementar la lógica para cerrar adecuandamente el cliente
                 * */
                System.out.println("Closing CLIENT context...");
                contextManager.changeContext(Context.SYSTEM);
                return true; //Ejecuta el comando, es reconocido

            case "?":
            case "--help":
                System.out.println("Available commands: " + getAvailableCommands());
                return true;
            /**
             * Implementar comandos de parte del cliente, como mkdir,ls,pwd,cd,scp -l <dir_local> -r <dir_remote>
             *     y otros más segun se nos ocurra. CADA COMANDO HA DE SER SOPORTADO/COMPATIBLE POR EL SERVIDOR.
             * */
            default:
                return false; //Comando no reconocido
        }
    }

    @Override
    public List<String> getAvailableCommands() {
        return Arrays.asList("--close", "--help", "?");
    }
}
