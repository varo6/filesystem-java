package com.filetransfer.common;

import com.filetransfer.client.ClientMain;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.List;
//import java.util.concurrent.TimeUnit;

/**
 * Consola inspirada de:
 * <a href="https://stackoverflow.com/questions/12945537/how-to-set-output-stream-to-textarea/12945678#12945678">Cómo hacer la ventana síncrona</a>
 */
public class ConsoleGUI {
    private ContextManager handler;

    public ConsoleGUI(ContextManager context) {
        handler = context;

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CapturePane capturePane = new CapturePane();
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(capturePane, BorderLayout.CENTER);

                JTextField inputField = new JTextField();
                frame.add(inputField, BorderLayout.SOUTH);

                // Escuchar el mensaje al presionar el botón enter
                inputField.addActionListener(e -> {
                    String userInput = inputField.getText();
                    if (userInput != null && !userInput.trim().isEmpty()) {
                        //Imprime el comando en la consola para que el usuario pueda verlo
                        System.out.println("> " + userInput);
                        String[] commandArgs = userInput.trim().split("\\s+");
                        if (handler.getCurrentContext() == Context.CLIENT) {
                            if (handler instanceof ClientMain) {
                                boolean sent = ((ClientMain) handler).sendMessage(commandArgs);
                                if (!sent) {
                                    System.err.println("No se pudo enviar el mensaje");
                                }
                            }
                        }
                        try {
                            handler.processCommand(commandArgs);
                        } catch (Exception ex) {
                            System.err.println("Error processing command: " + ex.getMessage());
                        }
                    }
                    inputField.setText("");
                });


                frame.setSize(600, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // Se redirigen los flujos de salida a la ventana, se pueden añadir todos los posibles, pero con estos son suficientes
                PrintStream ps = System.out;
                try {
                    System.setOut(new PrintStream(new StreamCapturer("Out", capturePane, ps), true));
                    System.setErr(new PrintStream(new StreamCapturer("Error", capturePane, ps), true));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                // Crear y lanzar un hilo para simular un proceso largo (si es necesario)
                new WorkerThread(capturePane).execute();
            }
        });
    }


    /**
     * Hilo de trabajo para ejecutar tareas en segundo plano
     */
    public class WorkerThread extends SwingWorker<Void, String> {
        private CapturePane capturePane;

        public WorkerThread(CapturePane capturePane) {
            this.capturePane = capturePane;
        }

        @Override
        protected Void doInBackground() throws Exception {

//            System.out.println("Hello, this is a test");
//            for (int i = 0; i < 3; i++) {
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                    // Publicar la salida mientras se ejecuta en segundo plano
//                    publish("hola:" + i);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            publish("Wave if you can see me");
            /**
             * Escribir en la pantalla todos los mensajes
             *
             * */
            // De momento no hay que añadir nada aquí puesto que los mensajes se envían desde otras clases.
            return null;
        }

        @Override
        protected void process(List<String> chunks) {

            for (String chunk : chunks) {
                capturePane.appendText(chunk + "\n");
            }
        }

        @Override
        protected void done() {
            //Mensajes iniciales tras arrancar la ventana
            System.out.println("Config loaded:");
            System.out.println("Server Port: " + handler.getPort());
            System.out.println("Default Server IP: " + handler.getAddress());
            System.out.println("Max Connections: " + handler.getMaxConnections());

            System.out.println("The console is ready to receive commands");
        }
    }

    /**
     * Captura y redirige la salida
     */
    /**
     * Esta clase es la parte visual de la captura de mensajes.
     */
    public class CapturePane extends JPanel implements Consumer {

        private JTextArea output;

        public CapturePane() {
            setLayout(new BorderLayout());
            output = new JTextArea();
            output.setBackground(new Color(223, 207, 190));
            output.setEditable(false); // Panel de solo lectura, registro de mensajes cliente-servidor
            add(new JScrollPane(output));
        }

        @Override
        public void appendText(final String text) {
            if (EventQueue.isDispatchThread()) {
                output.append(text);
                output.setCaretPosition(output.getText().length());
            } else {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        appendText(text);
                    }
                });
            }
        }
    }

    public interface Consumer {
        void appendText(String text);
    }

    /**
     * Clase que captura los flujos de salida
     */
    public class StreamCapturer extends OutputStream {

        private Writer writer;
        private StringBuilder buffer;
        private String prefix;
        private Consumer consumer;
        private PrintStream old;

        public StreamCapturer(String prefix, Consumer consumer, PrintStream old) throws UnsupportedEncodingException {
            this.prefix = prefix;
            buffer = new StringBuilder(128);
            buffer.append("[").append(prefix).append("] ");
            this.old = old;
            this.consumer = consumer;

            this.writer = new OutputStreamWriter(old, "UTF-8");
        }

        @Override
        public void write(int b) throws IOException {
            char c = (char) b;
            String value = Character.toString(c);
            buffer.append(value);
            if (value.equals("\n")) {
                consumer.appendText(buffer.toString());
                buffer.delete(0, buffer.length());
                buffer.append("[").append(prefix).append("] ");
            }

            writer.write(c);
            writer.flush();
        }
    }
}
