package common;

public class CloseMessage extends Header{

    protected final String CLOSE_MESSAGE= "close";


    public CloseMessage(int type, int length) {
        this.type = CLOSE;
    }
}
