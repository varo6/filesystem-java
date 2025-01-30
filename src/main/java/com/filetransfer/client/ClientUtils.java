package com.filetransfer.client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientUtils {

    public static boolean validateScpArg(String[] command) {
        if (command.length < 3) {
            System.out.println("Usage: scp -u/-d <source> <destination>");
            return false;
        }
        if (!command[1].equals("-u") && !command[1].equals("-d")) {
            System.out.println("Operation not supported. Use -u to upload or -d to download");
            return false;
        }
        // Para upload (-u): El archivo local (origen) debe existir
        if (command[1].equals("-u")) {
            Path localPath = Paths.get("FileSystem/storage").resolve(command[2]);
            if (!Files.exists(localPath)) {
                System.out.println("Error: Local file does not exist: " + command[2]);
                return false;
            }
        }
        return true;
    }
    public static boolean validateRmArg(String[] arg) {
        if (arg.length != 2 && arg.length != 3) {
            System.out.println("Invalid command. Usage: rm [file] [-r (optional)]");
            return false;
        }
        if (arg.length == 3 && !arg[2].equals("-r")) {
            System.out.println("Invalid command. Usage: rm [file] [-r (optional)]");
            return false;
        }

        return true;
    }

    public static boolean validateRnArg(String[] arg) {

        if (arg.length != 3) {
            System.out.println("Invalid command. Usage: rn <current> <new>");
            return false;
        }

        return true;
    }
    public static boolean validadeEchoArg(String[] arg) {
        if (arg.length < 2) {
            System.out.println("Invalid command. Usage: echo [message]");
            return false;
        } else if (arg.length > 2 && !arg[1].equals(">")) {
            return true;
        }
        return true;
    }
    public static boolean validateLsArg(String[] arg) {
        if (arg.length == 1) {
            return true;
        } else if (arg.length == 2 && arg[1].equals("-l")) {
            return true;
        } else {
            System.out.println("Invalid command. Usage: ls [ -l (optional) ]");
            return false;
        }
    }
    public static boolean validateMkdirArg(String[] arg) {
        if (arg.length != 2) {
            System.out.println("Invalid command. Usage: mkdir [directory]");
            return false;
        }
        return true;
    }

    public static boolean validatePwdArg(String[] arg) {
        if (arg.length != 1) {
            System.out.println("Invalid command. Usage: pwd");
            return false;
        }
        return true;
    }
    public static boolean validateCdArg(String[] arg) {
        if (arg.length != 2) {
            System.out.println("Invalid command. Usage: cd [directory]");
            return false;
        }
        return true;
    }
    public static boolean validateCheckConArg(String[] arg) {
        if (arg.length != 2 && arg.length != 1) {
            System.out.println("Invalid command. Usage: checkCon [message (optional)]");
            return false;
        }
        return true;
    }
}

