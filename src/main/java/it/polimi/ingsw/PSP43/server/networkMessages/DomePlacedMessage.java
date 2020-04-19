package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

public class DomePlacedMessage extends ResMessage {
    private static final long SerialVersionUID = 111111234567890123L;
    private Coord position;

    public DomePlacedMessage(int idGame, Coord position){
        super(idGame);
        this.position = position;
    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public Coord getPosition() {
        return position;
    }
}
