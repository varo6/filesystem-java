package com.filetransfer.server;

import com.filetransfer.common.CommandMessage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServerCommandProcess {

    private final Map<CommandMessage.CommandType, Function<Path, String>> commandHandlers;
    private CommandMessage cm;

    public ServerCommandProcess() {
        commandHandlers = new HashMap<>();
        commandHandlers.put(CommandMessage.CommandType.CON_CHECK, this::conCheck);
        commandHandlers.put(CommandMessage.CommandType.FILE_UPLOAD, this::fileUpload);
        commandHandlers.put(CommandMessage.CommandType.FILE_DOWNLOAD, this::fileDownload);
        commandHandlers.put(CommandMessage.CommandType.DIRECTORY_CREATE, this::directoryCreate);
        commandHandlers.put(CommandMessage.CommandType.DIRECTORY_LIST, this::directoryList);
        commandHandlers.put(CommandMessage.CommandType.DIRECTORY_LOCATION, this::directoryLocation);
        commandHandlers.put(CommandMessage.CommandType.FILE_DELETE, this::fileDelete);
        commandHandlers.put(CommandMessage.CommandType.DIRECTORY_OPEN, this::directoryOpen);
    }

    public String processCommand(CommandMessage cm, Path basePath) {
        this.cm = cm;
        Function<Path, String> handler = commandHandlers.get(cm.getCommandType());
        if (handler != null) {
            return handler.apply(basePath);
        } else {
            return "Comando no reconocido";
        }
    }

    private String conCheck(Path path) {
        return "Conexión con el cliente funcionando correctamente";
    }

    private String fileUpload(Path path) {
        return "Subiendo archivo a: " + path.toString();
    }

    private String fileDownload(Path path) {
        return "Descargando archivo de: " + path.toString();
    }

    private String directoryCreate(Path path) {
        try {
            Files.createDirectories(path.resolve(cm.getArgs().get(0)));
            return "Directorio creado en: " + path.toString();
        } catch (IOException e) {
            return "Error al crear el directorio: " + e.getMessage();
        }
    }

    private String directoryList(Path path) {
        try{
            String result = Files.list(path)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.joining("\n"));
        return "Listando directorio: " + path.toString() + "\n" + result;
        } catch (IOException e) {
            return "Error al listar el directorio: " + e.getMessage();
        }
    }

    private String directoryLocation(Path path) {
        return "Localización del directorio: " + path.toString();
    }

    private String fileDelete(Path path) {
        // Lógica para borrar un archivo
        return "Borrando archivo en: " + path.toString();
    }

    private String directoryOpen(Path path) {
        // Lógica para abrir un directorio
        return "Abriendo directorio: " + path.toString();
    }

}