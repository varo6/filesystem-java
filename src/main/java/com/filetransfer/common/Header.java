package common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class Header implements Serializable {
    protected int type;
    protected int length;
    protected static final int HEAD=0;
    protected static final int USER=1;
    protected static final int TEXT=2;
    protected static final int CLOSE=3;
    protected static final int INT = 4;
    protected static final int DOUBLE = 8;
    protected static final int CHAR = 2;
    protected static final int HEADER_LENGTH = INT*2;



    public Header() {
        this.length = HEADER_LENGTH;
        this.type = HEAD;
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

        byte[] self = new byte[HEADER_LENGTH];
        byteBuffer.get(self);

        return self;
    }

    public void unpack(byte[] a){
        ByteBuffer byteBuffer = ByteBuffer.wrap(a);

        type = byteBuffer.getInt();
        length = byteBuffer.getInt();
    }
}
