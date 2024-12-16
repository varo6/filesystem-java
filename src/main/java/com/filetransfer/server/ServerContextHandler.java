package com.filetransfer.server;

import com.filetransfer.common.Context;
import com.filetransfer.common.ContextCommandHandler;
import com.filetransfer.common.ContextManager;

import java.util.Arrays;
import java.util.List;

public class ServerContextHandler implements ContextCommandHandler {
    private ContextManager contextManager;

    public ServerContextHandler(ContextManager manager){
        this.contextManager = manager;
    }
    @Override
    public boolean handleCommand(String command) throws Exception {
        switch (command) {
            case "?":
            case "--help":
            case "-h":
                getAvailableCommands();
                return false;//False para no cerrar el contexto
            case "--close":
                /**
                 * Implementar la lógica para cerrar adecuandamente el servidor
                 * */
                contextManager.changeContext(Context.SYSTEM);
                return true;//True para cerrar

            /**
             * Añadir más commandos de parte del servidor para lo que haga falta, mismamente --logs
             * para generar un txt de todo lo sucedido, aunque en ese caso, hay que implementar
             * un sistema de GRABAR mensajes.
             * */

            default:
                return false;
        }
    }

    @Override
    public List<String> getAvailableCommands() {
        return Arrays.asList("--close");
    }
}
