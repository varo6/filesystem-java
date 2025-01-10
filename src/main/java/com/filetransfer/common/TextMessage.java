package com.filetransfer.common;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TextMessage extends Header {
    protected int dni = 0;
    protected String text = "";

    public TextMessage(int dni, String text) {
        super();
        this.type = Const.TYPE_TEXT;
        this.dni = dni;
        this.text = text;
    }

    public TextMessage(int dni) {
        super();
        this.type = Const.TYPE_TEXT;
        this.dni = dni;
    }

    public TextMessage(String text) {
        super();
        this.type = Const.TYPE_TEXT;
        this.text = text;
    }

    public TextMessage() {
        super();
        this.type = Const.TYPE_TEXT;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
