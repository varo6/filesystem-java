package com.filetransfer.server;

import java.io.*;
import java.net.Socket;

public class SimpleServer implements Runnable {

    private final Socket socket;

    public SimpleServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true)
        ) {
            String clientMessage;
            out.println("Server is ready to receive messages.");

            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.equals("quit")) {
                    break;
                }
                System.out.println("Recibido del cliente: " + clientMessage);
                String response = processCommand(clientMessage);
                out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Cliente desconectado");
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
            }
        }
    }
}
