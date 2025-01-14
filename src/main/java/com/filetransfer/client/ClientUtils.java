package com.filetransfer.client;

public class ClientUtils {

    public static boolean validateScpArg(String[] arg) {
        if (arg.length != 3) {
            System.out.println("Invalid command. Usage: scp [-u | -d] [file] ");
            return false;
        }
        if (arg.length == 3 && !arg[1].equals("-u") && !arg[1].equals("-d")) {
            System.out.println("Invalid command. Usage: scp [-u | -d] [file]");
            return false;
        }
        return true;
    }
    public static boolean validateRmArg(String[] arg) {
        if (arg.length != 2) {
            System.out.println("Invalid command. Usage: rm [file]");
            return false;
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

