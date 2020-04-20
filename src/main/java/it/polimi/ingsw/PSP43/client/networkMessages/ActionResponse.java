package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

public class ActionResponse extends ClientMessage {
    private static final long SerialVersionUID=543210987654321098L;
    private int idWorker;
    private Coord position;

    public ActionResponse(int idWorker, Coord position){
        this.idWorker = idWorker;
        this.position = position;
    }

    public int getIdWorker() {
        return idWorker;
    }

    public Coord getPosition() {
        return position;
    }
}
