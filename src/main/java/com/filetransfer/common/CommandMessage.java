package com.filetransfer.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandMessage extends Header implements Serializable {

    protected int type;
    protected List<String> args;
    private CommandType  mtype;
    private byte[] payload;

    public enum CommandType {
        FILE_UPLOAD,
        FILE_DOWNLOAD,
        DIRECTORY_CREATE,
        DIRECTORY_LIST,
        DIRECTORY_LOCATION,
        FILE_DELETE,
        DIRECTORY_OPEN,
        CON_CHECK
    }

    public CommandType getCommandType() {
        return mtype;
    }

    public String getCommandString(CommandType commandType){
        switch (commandType){
            case CON_CHECK:
                return "ccheck";
            case FILE_DELETE:
                return "rm";
            case FILE_UPLOAD:
                return "scp";
            case FILE_DOWNLOAD:
                return "scp";
            case DIRECTORY_LIST:
                return "pwd";
            case DIRECTORY_OPEN:
                return "cd";
            case DIRECTORY_CREATE:
                return "mkdir";
            case DIRECTORY_LOCATION:
                return "ls";
        }
    }
    public List<String> getArgs() {
        return new ArrayList<>(args);
    }

    public byte[] getPayload() {
        return payload;
    }

    private CommandMessage(Builder builder) {
        super();
        setType(Const.TYPE_COMMAND);
        this.type = Const.TYPE_COMMAND;
        this.mtype = builder.cType;
        this.args = builder.args;
        this.payload = builder.payload;
    }

    public static class Builder{
        private CommandType cType;
        private List<String> args = new ArrayList<>();
        private byte[] payload;

        public Builder(CommandType commandType){
            cType = commandType;
        }

        public Builder addArg(String arg) {
            this.args.add(arg);
            return this;
        }

        public Builder setPayload(byte[] payload) {
            this.payload = payload;
            return this;
        }
        public CommandMessage build() throws IllegalArgumentException {
            return new CommandMessage(this);
        }

    }
}
