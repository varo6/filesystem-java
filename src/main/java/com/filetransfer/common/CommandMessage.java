package com.filetransfer.common;

import java.io.Serializable;
import java.util.List;

public class CommandMessage extends Header implements Serializable  {

    protected int type;
    protected List<String> args;
    private MessageType mtype;
    private String command;
    private byte[] payload;

    public enum MessageType {
        FILE_UPLOAD,
        FILE_DOWNLOAD,
        DIRECTORY_CREATE,
        DIRECTORY_LIST,

    }
    public CommandMessage(List<String> args) {
        super();
        this.type = Const.TYPE_COMMAND;
        this.args = args;
    }
}
