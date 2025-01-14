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
    private Path clientPath =  Paths.get("");

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
        Path fullPath = basePath.resolve(clientPath);
        Function<Path, String> handler = commandHandlers.get(cm.getCommandType());
        if (handler != null) {
            return handler.apply(fullPath);
        } else {
            return "Comando no reconocido";
        }
    }

    private String conCheck(Path path) {
        return "Conexión con el cliente funcionando correctamente";
    }

    private String fileUpload(Path path) {
        byte[] pd = cm.getPayload();
        Path filename = Paths.get(""+cm.getArgs().get(0));
        path = path.resolve(filename);
        try {
            Files.write(path, pd);

        }
        catch(IOException e){
            return "error"+e.getMessage();

        }

        return "Subiendo archivo a: " + path.toString();
    }

    private String fileDownload(Path path) {
        return "Descargando archivo de: " + path.toString();
    }

    private String directoryCreate(Path path) {
        Path newDir = path.resolve(cm.getArgs().get(0));
        if (Files.exists(newDir)) {
            return "El directorio ya existe: " + newDir.toString();
        } else {
            try {
                Files.createDirectories(newDir);
                return "Directorio creado en: " + newDir.toString();
            } catch (IOException e) {
                return "Error al crear el directorio: " + e.getMessage();
            }
        }
    }

    private String directoryList(Path path) {
        try{
            String result = "";
            if (cm.getArgs().isEmpty()){
                result = Files.list(path)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .collect(Collectors.joining(" "));
            }
            else if (cm.getArgs().get(0).equals("-l")) {
                result = Files.list(path)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .collect(Collectors.joining("\n"));
            }
        return "Listando directorio: " + path.toString() + "\n" + result;
        } catch (IOException e) {
            return "Error al listar el directorio: " + e.getMessage();
        }
    }

    private String directoryLocation(Path path) {
        return "Localización del directorio: \n" + path.toString();
    }

    private String fileDelete(Path path) {
        Path file = path.resolve(cm.getArgs().get(0));
        try {
            if (Files.exists(file)) {
                Files.delete(file);
                return "Archivo" + cm.getArgs().get(0) + "borrado en: " + path.toString();
            } else {
                return "Error: El archivo no existe en " + path.toString();
            }
        } catch (IOException e) {
            return "Error al borrar el archivo: " + e.getMessage();
            }
    }

    private String directoryOpen(Path path) {
        String arg = cm.getArgs().get(0);

        switch (arg) {
            case "..":
                if (clientPath.toString().isEmpty()) {
                    return "Estás en la raíz del proyecto. No se puede subir más.";
                } else {
                    clientPath = clientPath.getParent();
                    if (clientPath==null){clientPath = Paths.get("");}

                    return "Subiendo un nivel de carpeta. Nueva ruta: " + clientPath;
                }
            case ".":
                return "Permaneciendo en el directorio actual: " + clientPath;
            default:
                // Manejar otros casos (por ejemplo, cambiar a un directorio específico)
                path = path.resolve(arg);
                if (Files.isDirectory(path)) {
                    this.clientPath = clientPath.resolve(arg);
                    return "Cambiando al directorio: " + clientPath;
                } else {
                    return "La ruta especificada no es un directorio válido.";
                }
        }
    }
}