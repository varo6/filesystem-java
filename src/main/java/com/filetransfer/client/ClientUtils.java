package com.filetransfer.client;

public class ClientUtils {
    public static boolean validateLsArg(String arg) {
        if ( !arg.equals("-la") && !arg.equals("-l") && !arg.equals("-a")) {
            System.out.println("Invalid command. Usage: ls");
            return false;
        }
        return true;
    }
}

