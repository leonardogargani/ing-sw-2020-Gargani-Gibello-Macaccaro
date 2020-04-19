package it.polimi.ingsw.PSP43.client.networkMessages;

import java.io.Serializable;

public abstract class ReqMessage implements Serializable {
    private String nick;

    public ReqMessage(String nick){
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
}
