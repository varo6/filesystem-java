import java.io.*;
import java.net.*;

public class SimpleServer {

    public void runServer(int port) throws IOException {
        while (true) {
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
                String str = in.readLine();
                if (str == null || str.equals("quit")) {
                    break;
                }
                System.out.println("Echoing: " + str);
                out.println(str);
            }
        }
    }
}
