package common;

import java.io.Serializable;
import java.util.List;

public class CommandMessage extends Header implements Serializable  {

    protected int type;
    protected List<String> args;

    public CommandMessage(List<String> args) {
        super();
        this.type = Const.TYPE_COMMAND;
        this.args = args;
    }
}
