package com.filetransfer.client;

public class ClientUtils {

    public static boolean validateScpArg(String[] arg) {
        if (arg.length != 3 && arg.length != 4) {
            System.out.println("Invalid command. Usage: scp [-u | -d] [file] [-r (optional)]");
            return false;
        }
        if (arg.length == 4 && !arg[3].equals("-r")) {
            System.out.println("Invalid command. Usage: scp [-u | -d] [file] [-r (optional)]");
            return false;
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
    public static boolean validateLsArg(String[] arg) {
        if (arg.length == 1) {
            return true;
        } else if (arg.length == 2) {
            return arg[1].equals("-la") || arg[1].equals("-l") || arg[1].equals("-a");
        } else {
            System.out.println("Invalid command. Usage: ls [-la | -l | -a]");
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

