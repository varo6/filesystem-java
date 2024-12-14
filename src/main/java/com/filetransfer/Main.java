import client.ClientMain;
import server.ServerMain;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String[] input;

        if (args.length == 0) {
            System.out.println("Por favor, proporciona un argumento: --client o --server o --exit");
            System.out.print("Introduce la opción: ");
            String userInput = scanner.nextLine();
            input = userInput.split(" ");
        } else {
            input = args;
        }

        if (input.length == 0) {
            System.out.println("No se proporcionaron argumentos válidos.");
            return;
        }

        switch (input[0]) {
            case "--client":
            case "-c":
                System.out.println("Iniciando cliente...");
                if (input.length == 3) {
                    String address = input[1];
                    try {
                        int port = Integer.parseInt(input[2]);
                        ClientMain c = new ClientMain(address, port);
                        c.start();
                    } catch (NumberFormatException e) {
                        System.err.println("Error: El puerto debe ser un número entero válido.");
                    }
                } else {
                    ClientMain c = new ClientMain();
                    c.start();
                }
                break;

            case "--server":
            case "-s":
                System.out.println("Iniciando servidor...");
                if (input.length == 2) {
                    try {
                        int port = Integer.parseInt(input[1]);
                        ServerMain s = new ServerMain(port);
                        s.start();
                    } catch (NumberFormatException e) {
                        System.err.println("Error: El puerto debe ser un número entero válido.");
                    }
                } else {
                    ServerMain s = new ServerMain();
                    s.start();
                }
                break;

            case "--exit":
            case "-e":
                System.out.println("Cerrando programa.");
                break;

            default:
                System.err.println("Argumento no reconocido. Usa --client, --server o --exit.");
                break;
        }

        scanner.close();
    }
}
