package com.filetransfer;

import com.filetransfer.common.ConsoleGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {
        //se usará la consola por defecto si no se indica lo contrario
        boolean useGui = true;
        for (String arg : args) {
            if (arg.equals("--nogui")) {
                useGui = false;
                break;
            }
        }

        FileSystem fileSystem = new FileSystem();

        //Ejecución biblioteca gráfica
        if (!useGui) {
            fileSystem.runConsoleMode(args);
        } else {
            SwingUtilities.invokeLater(() -> {
                try {
                    /**ConsoleGUI gui = new ConsoleGUI(fileSystem);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
