package com.filetransfer.common;

import com.filetransfer.client.ClientContextHandler;
import com.filetransfer.server.ServerContextHandler;
import com.filetransfer.SystemContextHandler;

import java.util.HashMap;
import java.util.Map;
/**
 * ContextManager es la clase que se encarga de almacenar variables del archivo, al igual que
 * almacena el contexto del usuario actuando como una máquina de estados gracias al enum Context
 * */
public class ContextManager {
    private Context currentContext = Context.SYSTEM;
    private ContextCommandHandler currentHandler;

    public Thread getActiveThread() {
        return activeThread;
    }

    public void setActiveThread(Thread activeThread) {
        this.activeThread = activeThread;
    }

    public void stopActiveThread() {
        if (activeThread != null && activeThread.isAlive()) {
            activeThread.interrupt();
            try {
                activeThread.join(1000);
            } catch (InterruptedException e) {
                System.err.println("Error stopping thread: " + e.getMessage());
            }
            System.out.println("Thread stopped successfully.");
        }
        activeThread = null;
    }


    private Thread activeThread;
    private int port;
    private int maxConnections;
    String address;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private Map<Context, ContextCommandHandler> contextHandlers;

    public ContextManager() {
        contextHandlers = new HashMap<>();
        contextHandlers.put(Context.SYSTEM, new SystemContextHandler(this));
        contextHandlers.put(Context.CLIENT, new ClientContextHandler(this));
        contextHandlers.put(Context.SERVER, new ServerContextHandler(this));

        // Default
        currentHandler = contextHandlers.get(Context.SYSTEM);
    }

    /**
     * Procesa un comando dependiendo del contexto actual
     */
    public void processCommand(String command) throws Exception {
        if (!currentHandler.handleCommand(command)) {
            System.out.println("Command not supported in " + currentContext);
        }
    }

    public void changeContext(Context newContext) {
        if (contextHandlers.containsKey(newContext)) {
            currentContext = newContext;
            currentHandler = contextHandlers.get(newContext);
            System.out.println("Switched to context: " + newContext);
        } else {
            System.out.println("Unknown context: " + newContext);
        }
    }

    public Context getCurrentContext() {
        return currentContext;
    }
}
