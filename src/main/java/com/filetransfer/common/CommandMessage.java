package com.filetransfer.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandMessage extends Header implements Serializable {

    protected int type;
    protected List<String> args;
    private CommandType  mtype;
    private byte[] payload;

    public enum CommandType  {
        FILE_UPLOAD(2),
        FILE_DOWNLOAD(2),
        DIRECTORY_CREATE(1),
        DIRECTORY_LIST(0);

        private final int requiredArgs;

        CommandType(int requiredArgs) {
            this.requiredArgs = requiredArgs;
        }

        public int getRequiredArgs() {
            return requiredArgs;
        }

    }

    public CommandType getCommandType() {
        return mtype;
    }

    public List<String> getArgs() {
        return new ArrayList<>(args);
    }

    public byte[] getPayload() {
        return payload;
    }

    private CommandMessage(Builder builder) {
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
            validateCommand();
            return new CommandMessage(this);
        }

        private void validateCommand() {
            if (args.size() != cType.getRequiredArgs()) {
                throw new IllegalArgumentException(
                        String.format("Command %s requires %d arguments, but got %d",
                                cType, cType.getRequiredArgs(), args.size())
                );
            }
        }
    }
}
