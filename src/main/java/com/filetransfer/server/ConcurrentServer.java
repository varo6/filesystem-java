package server;


import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentServer implements Runnable, Serializable {

    //PASAR A ENUMS
    private static final int MAX_THREADS = 10;
    private final int port;
    private ServerSocket serverSocket;
    private ExecutorService pool;

    public ConcurrentServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.pool = Executors.newFixedThreadPool(MAX_THREADS);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket clientsocket = serverSocket.accept();
                // lambda expression
                pool.execute(() -> {
                    try {
                        new SimpleServer(clientsocket).run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
