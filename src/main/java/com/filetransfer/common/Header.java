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

    public byte[] pack() throws IOException {


        //La cabecera solo tiene un tamaño de 4 + 4 = 8
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);

        outputStream.writeObject(this);

        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        byteBuffer.putInt(type);
        byteBuffer.putInt(length);

        byteBuffer.flip();

        byte[] self = new byte[Const.HEADER_LENGTH];
        byteBuffer.get(self);

        return self;
    }

    public void unpack(byte[] a){
        ByteBuffer byteBuffer = ByteBuffer.wrap(a);

        type = byteBuffer.getInt();
        length = byteBuffer.getInt();
    }
}
