public class ServerMain {

    // Puerto por defecto de 8080 por si no se le pasa en el constructor.
    public ServerMain() throws Exception {
        this(8080);
    }

    public ServerMain(int port) throws Exception {
        System.out.println("Server started listening on port " + port);
        ConcurrentServer cs = new ConcurrentServer(port);
        cs.run();
    }
}
