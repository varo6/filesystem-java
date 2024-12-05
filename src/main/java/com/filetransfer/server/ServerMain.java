package server;

public class ServerMain {

    // Puerto por defecto de 8080 por si no se le pasa en el constructor.
    public ServerMain() {
        this(8080);
    }

    public ServerMain(int port) {
        System.out.println("Server started listening on port " + port);
        ConcurrentServer cs = new ConcurrentServer(port);
        cs.run();
    }
}
