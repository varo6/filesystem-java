import java.io.*;
import java.net.*;

public class SimpleClient {

    private final int port;
    private final String host;

    public SimpleClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void runClient() throws UnknownHostException, IOException {
        Socket socket = new Socket(host, port);

        try {
            System.out.println("connecting to server with = " + socket);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            PrintWriter out = new PrintWriter(
                new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
                ),
                true
            );

            while (true) {
                System.out.println("Waiting for input from keyboard ");
                BufferedReader press = new BufferedReader(
                    new InputStreamReader(System.in)
                );
                String s = press.readLine();
                System.out.println("Sending to server: " + s);
                out.println(s);

                if (s == null || s.equals("quit")) {
                    break;
                }
                String str = in.readLine();
                if (str == null) {
                    break;
                } //Server has closed
                System.out.println("Received from server: " + str);
            }
        } finally {
            System.out.println("closing...");

            socket.close();
        }
    }
}
