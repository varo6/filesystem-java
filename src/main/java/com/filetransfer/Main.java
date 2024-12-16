package com.filetransfer;


public class Main {
/**
 * ERRORES A SOLUCIONAR:
 * 1.Desde la GUI no se puede conectar al servidor con el cliente "Controlador no válido"
 * 2.Al crear un listener en FileSystem y otro en SimpleClient, desde el CLI hay que reenviar el mensaje
 * dos veces para que llegue al servidor
 * */
    public static void main(String[] args) throws Exception {
        FileSystem fileSystem = new FileSystem(args);
    }
}
