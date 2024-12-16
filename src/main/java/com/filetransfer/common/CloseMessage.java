package com.filetransfer.common;

public class CloseMessage extends Header {

    protected final String CLOSE_MESSAGE = "close";


    public CloseMessage(int type, int length) {
        this.type = Const.TYPE_CLOSE;
    }
}
