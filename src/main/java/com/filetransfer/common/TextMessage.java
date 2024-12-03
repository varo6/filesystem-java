package common;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TextMessage extends Header{
    protected int dni = 0;
    protected String text = "";

    public TextMessage(int dni, String text) {
        super();
        this.type = TEXT;
        this.dni = dni;
        this.text = text;
    }

    public TextMessage(int dni) {
        super();
        this.type = TEXT;
        this.dni = dni;
    }

    public TextMessage(String text) {
        super();
        this.type = TEXT;
        this.text = text;
    }

    public TextMessage() {
        super();
        this.type = TEXT;
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

    @Override
    public byte[] pack() throws IOException {
        //Antes de empaquetar el mensaje, se ha empaquetado la cabecera que indicará que es de tipo Text
        length = INT+text.length()*CHAR;
        byte[] self = new byte[INT*2+length];

        ByteBuffer byteBuffer = ByteBuffer.allocate(INT*2+length);
        byteBuffer.putInt(type);//El orden es importante que se mantenga
        byteBuffer.putInt(length);
        byteBuffer.putInt(getDni());
        char[] textChar = getText().toCharArray();
        //No existe un metodo para empaquetar un texto de forma directa, la pasamos a un array de Char y la introducimos
        for (char c: textChar){
            byteBuffer.putChar(textChar[c]);
        }

        return self;
    }

    @Override
    public void unpack(byte[] a) {
        // super.unpack(a); No hace falta si lo que queremos es leer la parte del cuerpo Text,
        // con el super se ha de incluir el tamaño de la cabecera
        ByteBuffer byteBuffer = ByteBuffer.wrap(a);

        dni = byteBuffer.getInt();
        int textSize=(length-4)/2;
        char [] chars= new char[textSize];
        for (int i=0; i < chars.length; i++){
            chars[i] = byteBuffer.getChar();
        }

        text = chars.toString();
        //Se ha desempaquetado la cabecera
        //El desempaque del cuerpo depende de cómo ha sido empaquetado este Objeto: DNI + TEXTO
    }
}
