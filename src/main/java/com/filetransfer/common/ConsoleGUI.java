package com.filetransfer.common;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.List;
//import java.util.concurrent.TimeUnit;
/**
 * <a href="https://stackoverflow.com/questions/12945537/how-to-set-output-stream-to-textarea/12945678#12945678">Cómo hacer la ventana síncrona</a>
 * */
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

                /**
                 * Escuchar el botón enter
                 * */
                inputField.addActionListener(e -> {
                    String userInput = inputField.getText();
                    capturePane.appendText("> " + userInput + "\n");
                    inputField.setText("");


                    if (userInput != null && !userInput.trim().isEmpty()) {
//                        handler.executeCommand(userInput.trim());
                    }
                });

                frame.setBackground(new Color(223, 207, 190));
                frame.setSize(600, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                /**
                 * Todos los mensajes de consola se redirigen a la ventana
                 * */
                PrintStream ps = System.out;
                try {
                    System.setOut(new PrintStream(new StreamCapturer("Out", capturePane, ps), true));
                    System.setErr(new PrintStream(new StreamCapturer("Error", capturePane, ps), true));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                // Crear y lanzar un hilo para simular un proceso largo
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


//            System.out.println("Config loaded:");
//            System.out.println("Server Port: " + handler.getPort());
//            System.out.println("Default Server IP: "+ handler.getAddress());
//            System.out.println("Max Connections: " + handler.getMaxConnections());

            System.out.println("The console is ready to receive commands");
        }
    }

    /**
     * Captura y redirige la salida
     */

    public class CapturePane extends JPanel implements Consumer {

        private JTextArea output;

        public CapturePane() {
            setLayout(new BorderLayout());
            output = new JTextArea();
            output.setEditable(false); // Panel de solo lectura
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