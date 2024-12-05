public class ClientMain {

    public ClientMain() throws Exception {
        this("localhost", 8080);
    }

    public ClientMain(String host, int port) throws Exception {
        System.out.println("Client started connecting to " + host + ":" + port);
        SimpleClient c = new SimpleClient(host, port);
        c.runClient();
    }
}
