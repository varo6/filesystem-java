package com.filetransfer.server;

import com.filetransfer.common.ContextManager;
import com.filetransfer.common.Utils;

import java.io.IOException;
import java.util.Scanner;

public class ServerMain extends ContextManager {
    private int port;
    private String publicIP;
    private String privateIP;

    public ServerMain(int port) {
        this.port = port;
        publicIP = Utils.getPublicIP();
        privateIP = Utils.getPrivateIP();
    }

    public void start() throws IOException {
        System.out.println("Server started listening on port " + port);
        System.out.println("Public Network: " + publicIP);
        System.out.println("Local Network: " + privateIP);
        ConcurrentServer cs = new ConcurrentServer(port);

        new Thread(this::runCommandLoop);

        cs.run();
    }

    private void runCommandLoop() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("server> ");
            String input = scanner.nextLine().trim();
            //executeCommand(input);
        }
    }
}
