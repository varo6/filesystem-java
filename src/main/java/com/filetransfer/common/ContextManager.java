package com.filetransfer.common;

import com.filetransfer.client.ClientContextHandler;
import com.filetransfer.server.ServerContextHandler;
import com.filetransfer.SystemContextHandler;

import java.util.HashMap;
import java.util.Map;

public class ContextManager {
    private Context currentContext = Context.SYSTEM;
    private ContextCommandHandler currentHandler;

    private Map<Context, ContextCommandHandler> contextHandlers;

    public ContextManager() {
        contextHandlers = new HashMap<>();
        contextHandlers.put(Context.SYSTEM, new SystemContextHandler());
        contextHandlers.put(Context.CLIENT, new ClientContextHandler());
        contextHandlers.put(Context.SERVER, new ServerContextHandler());

        // Default
        currentHandler = contextHandlers.get(Context.SYSTEM);
    }

    /**
     * Procesa un comando dependiendo del contexto actual
     */
    public void processCommand(String command) throws Exception {
        if (currentHandler.handleCommand(command)) {
            switch (command) {
                case "--client":
                    currentContext = Context.CLIENT;
                    currentHandler = contextHandlers.get(Context.CLIENT);
                    System.out.println("Cambiado a contexto CLIENT");
                    break;
                case "--server":
                    currentContext = Context.SERVER;
                    currentHandler = contextHandlers.get(Context.SERVER);
                    System.out.println("Cambiado a contexto SERVER");
                    break;
                case "--exit":
                    // Permite volver a SYSTEM
                    currentContext = Context.SYSTEM;
                    currentHandler = contextHandlers.get(Context.SYSTEM);
                    System.out.println("Regresado a contexto SYSTEM");
                    break;
                default:
                    System.out.println("Comando no reconocido en contexto " + currentContext);
            }
        } else {
            System.out.println("Comando no válido en contexto " + currentContext);
        }
    }

    public Context getCurrentContext() {
        return currentContext;
    }
}
