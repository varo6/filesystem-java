package server;

public class ServerMain {

    public ServerMain() {
        //comprobamos si la carpeta storage está creada
        //server listening on....
        ConcurrentServer cs = new ConcurrentServer();
        cs.run();
    }
}
