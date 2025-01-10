package com.filetransfer.client;

public class ClientUtils {
    public static boolean validateLsArg(String[] arg) {
        if (arg.length != 2 && !arg[1].equals("-la") && !arg[1].equals("-l") && !arg[1].equals("-a")) {
            System.out.println("Invalid command. Usage: ls");
            return false;
        }
        return true;
    }
}

