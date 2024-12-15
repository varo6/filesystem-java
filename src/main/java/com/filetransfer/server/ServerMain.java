package com.filetransfer.server;

import com.filetransfer.common.Utils;
import java.io.IOException;

public class ServerMain {
    private int port;
    private String publicIP;
    private String privateIP;

    public ServerMain() throws Exception {
        this(8080);
        publicIP = Utils.getPublicIP();
        privateIP = Utils.getPrivateIP();
    }

    public ServerMain(int port) {
        this.port = port;
        publicIP = Utils.getPublicIP();
        privateIP = Utils.getPrivateIP();
    }

    public void start() throws IOException {
        System.out.println("Server started listening on port " + port);
        System.out.println("Local Network: " + publicIP);
        System.out.println("Public Network: " + privateIP);
        ConcurrentServer cs = new ConcurrentServer(port);
        cs.run();
    }
}
