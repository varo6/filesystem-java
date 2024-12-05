import java.io.*;
import java.net.*;

public class SimpleServer implements Runnable {

    private final Socket socket;

    public SimpleServer(Socket socket) throws Exception {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
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
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
