package com.filetransfer.server;

import com.filetransfer.client.ClientContextHandler;
import com.filetransfer.common.Const;
import com.filetransfer.common.Context;
import com.filetransfer.common.ContextManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentServer implements Runnable {

    private final int port;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private ServerContextHandler serverHandler;

    public ConcurrentServer(int port,  ContextManager contextManager) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.pool = Executors.newFixedThreadPool(Const.MAX_THREADS);
        this.serverHandler = (ServerContextHandler) contextManager.getContextHandler(Context.SERVER);
    }

    @Override
    public void run() {
        System.out.println("Servidor aceptando conexiones en el puerto " + port);
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clientSocket.getRemoteSocketAddress());

                pool.execute(() -> {
                    try {
                        new SimpleServer(clientSocket).run();

                    } catch (Exception e) {
                        System.err.println("Error al manejar la conexión del cliente: " + e.getMessage());
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Error al aceptar conexiones: " + e.getMessage());
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del servidor: " + e.getMessage());
            }
        }
    }
}
