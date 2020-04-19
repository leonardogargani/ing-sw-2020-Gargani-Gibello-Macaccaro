package it.polimi.ingsw.PSP43.server.networkMessages;

import java.io.Serializable;

public abstract class ResMessage implements Serializable {
    private int idGame;

    public ResMessage(int idGame){
        this.idGame=idGame;
    }

    public int getIdGame() {
        return idGame;
    }
}
