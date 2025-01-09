package com.filetransfer.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class Header implements Serializable {
    protected int type;
    protected int length;


    public Header() {
        this.length = Const.HEADER_LENGTH;
        this.type = Const.TYPE_HEADER;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
