package com.filetransfer.common;

import java.util.List;

public interface ContextCommandHandler {
    boolean handleCommand(String[] command) throws Exception;

    List<String> getAvailableCommands();
}
