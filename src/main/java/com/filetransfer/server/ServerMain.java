package server;

import java.io.IOException;

public class ServerMain {
    private int port;
    private String host;

    // Puerto por defecto de 8080 por si no se le pasa en el constructor.
    public ServerMain() throws Exception {
        this(8080);
    }

    public ServerMain(int port){
        this.port = port;
    }
    public void start() throws IOException {
        System.out.println("Server started listening on port " + port + "Address:");
        ConcurrentServer cs = new ConcurrentServer(port);
        cs.run();
    }
}
