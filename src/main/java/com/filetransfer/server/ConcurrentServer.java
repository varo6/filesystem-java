import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ConcurrentServer implements Runnable, Serializable {

    //PASAR A ENUMS
    private static final int MAX_THREADS = 10;
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService pool;

    public ConcurrentServer() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.pool = Executors.newFixedThreadPool(MAX_THREADS);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket clientsocket = serverSocket.accept();
                //VER QUE EJECUTAMOS POR HILO. CAMBIAR------------------
                pool.execute(new FileServer());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
