package com.filetransfer.server;

import com.filetransfer.common.CommandMessage;

import java.io.File;
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
        commandHandlers.put(CommandMessage.CommandType.FILE_RENAME, this::fileRename);
        commandHandlers.put(CommandMessage.CommandType.ECHO_FILE, this::echoFile);
    }

    public String processCommand(CommandMessage cm, Path basePath) {
        this.cm = cm;
        Path fullPath = basePath.resolve(clientPath);
        Function<Path, String> handler = commandHandlers.get(cm.getCommandType());
        if (handler != null) {
            return handler.apply(fullPath);
        } else {
            return "Command not recognized";
        }
    }

    private String conCheck(Path path) {
        return "Connection check successful";
    }

    private String fileUpload(Path path) {
        try {
            String remotePath = cm.getArgs().get(2);
            Path fullPath = path.resolve(remotePath);
            byte[] fileContent = cm.getPayload();

            Files.createDirectories(fullPath.getParent());

            Files.write(fullPath, fileContent);
            return "File uploaded successfully: " + fullPath;
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }

    private String fileDownload(Path path) {
        try {
            String remotePath = cm.getArgs().get(1);
            Path fullPath = path.resolve(remotePath);

            if (!Files.exists(fullPath)) {
                return "[Error]: File not found";
            }

            byte[] fileContent = Files.readAllBytes(fullPath);
            CommandMessage response = new CommandMessage.Builder(CommandMessage.CommandType.FILE_DOWNLOAD)
                    .addArg("file")
                    .setPayload(fileContent)
                    .build();
            return "File found and ready to download";
        } catch (IOException e) {
            return "Failed to read file: " + e.getMessage();
        }
    }

    private String directoryCreate(Path path) {
        Path newDir = path.resolve(cm.getArgs().get(0));
        if (Files.exists(newDir)) {
            return "Directory already exists: " + newDir.toString();
        } else {
            try {
                Files.createDirectories(newDir);
                return "Directory created in: " + newDir.toString();
            } catch (IOException e) {
                return "Failed to create directory: " + e.getMessage();
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
        return "Listing directories: " + path.toString() + "\n" + result;
        } catch (IOException e) {
            return "Failed listing: " + e.getMessage();
        }
    }

    private String directoryLocation(Path path) {
        return "File localization: \n" + path.toString();
    }

    private String fileDelete(Path path) {
        Path file = path.resolve(cm.getArgs().get(0));
        try {
            if (Files.exists(file)) {
                Files.delete(file);
                return "File " + cm.getArgs().get(0) + "successfully deleted: " + path.toString();
            } else {
                return "Error: File does not exist " + path.toString();
            }
        } catch (IOException e) {
            return "Failed deleting file " + e.getMessage();
            }
    }

    private String fileRename(Path path) {
        Path oldFile = path.resolve(cm.getArgs().get(0));
        Path newFile = path.resolve(cm.getArgs().get(1));
        try {
            if (Files.exists(oldFile)) {
                Files.move(oldFile, newFile);
                return "Renamed file " + oldFile + " to " + newFile;
            } else {
                return "Error: File does not exist " + path.toString();
            }
        } catch (IOException e) {
            return "Failed to rename file: " + e.getMessage();
        }
    }

    private String echoFile(Path path) {
        try {
            Path filePath = path.resolve(cm.getArgs().get(0));
            if (!Files.exists(filePath)) {
                return "File not found: " + filePath.getFileName();
            }

            if (!Files.isReadable(filePath)) {
                return "Cannot read file: " + filePath.getFileName();
            }

            return Files.readString(filePath);

        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        } catch (IndexOutOfBoundsException e) {
            return "No file specified";
        }
    }

    private String directoryOpen(Path path) {
        String arg = cm.getArgs().get(0);

        switch (arg) {
            case "..":
                if (clientPath.toString().isEmpty()) {
                    return "Already in root directory.";
                } else {
                    clientPath = clientPath.getParent();
                    if (clientPath==null){clientPath = Paths.get("");}

                    return "New path: " + clientPath;
                }
            case ".":
                return "Remaining in current directory" + clientPath;
            default:
                // Manejar otros casos (por ejemplo, cambiar a un directorio específico)
                path = path.resolve(arg);
                if (Files.isDirectory(path)) {
                    this.clientPath = clientPath.resolve(arg);
                    return "Changing directory: " + clientPath;
                } else {
                    return "Specific route not found.";
                }
        }
    }
}