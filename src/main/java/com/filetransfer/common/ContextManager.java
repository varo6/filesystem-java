package com.filetransfer.common;

import com.filetransfer.SystemContextHandler;
import com.filetransfer.client.ClientContextHandler;
import com.filetransfer.server.ServerContextHandler;

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

        currentHandler = contextHandlers.get(Context.SYSTEM);
    }

    public void processCommand(String command) throws Exception {
        if (currentHandler.handleCommand(command)) {
            switch(command) {
                case "--client":
                    currentContext = Context.CLIENT;
                    currentHandler = contextHandlers.get(Context.CLIENT);
                    break;
                case "--server":
                    currentContext = Context.SERVER;
                    currentHandler = contextHandlers.get(Context.SERVER);
                    break;
            }
        }
    }

    public Context getCurrentContext() {
        return currentContext;
    }
}