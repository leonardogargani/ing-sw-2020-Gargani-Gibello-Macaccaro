package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

public class HeightChangeMessage extends ResMessage {
    private static final long SerialVersionUID=111112345678901234L;
    private int height;
    private Coord position;

    public HeightChangeMessage(int idGame, int height, Coord position){
        super(idGame);
        this.height=height;
        this.position=position;
    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public int getHeight() {
        return height;
    }

    public Coord getPosition() {
        return position;
    }
}
