package client;

public class ClientMain {

    public ClientMain() {
        this("localhost", 8080);
    }

    public ClientMain(String host, int port) {
        System.out.println("Client started connecting to " + host + ":" + port);
        Client c = new SimpleClient(host, port);
        c.run();
    }
}
