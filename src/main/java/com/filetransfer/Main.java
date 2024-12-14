import client.ClientMain;
import server.ServerMain;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String[] input = new String[10];

        if (args.length == 0) {
            System.out.println("Por favor, proporciona un argumento: --client o --server o --exit");
            System.out.print("Introduce la opción: ");
            /**
             * ITERAR PARA LEER TODOS LOS PARÁMETROS DE ENTRADA
             * */
            input[0] = scanner.nextLine();
        } else {
            input = args;
        }

        switch (input[0]) {
            case "--client":
            case "-c":
                System.out.println("Iniciando cliente.");
                /**
                 * PREGUNTAR POR IP:PUERTO
                 * if...
                 * */
                ClientMain c = new ClientMain();
                c.start();
                break;
            case "--server":
            case "-s":
                System.out.println("Iniciando servidor.");
                /**
                 * PREGUNTAR POR PUERTO
                 * if...
                 * */
                ServerMain s = new ServerMain();
                s.start();
                break;
            case "--exit":
            case "-e":
                System.out.println("Cerrando programa.");
                break;
            default:
                throw new IllegalArgumentException("Argumento no reconocido, por favor inicia de nuevo el programa");
        }
    }
}
