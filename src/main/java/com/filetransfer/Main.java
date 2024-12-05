public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println(
                "Por favor, proporciona un argumento: --client o --server"
            );
            return;
        }

        switch (args[0]) {
            case "--client":
            case "-c":
                ClientMain c = new ClientMain();
                break;
            case "--server":
            case "-s":
                //Iniciamos servidor
                ServerMain s = new ServerMain();
                break;
            default:
                System.out.println(
                    "Argumento no reconocido. Usa --client o --server"
                );
                break;
        }
    }
}
