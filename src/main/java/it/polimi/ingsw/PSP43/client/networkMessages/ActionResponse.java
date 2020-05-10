package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

public class ActionResponse extends ClientMessage {
    private static final long SerialVersionUID=543210987654321098L;
    private Coord workerPosition;
    private Coord position;

    public ActionResponse(Coord workerPosition, Coord position){
        this.workerPosition = workerPosition;
        this.position = position;
    }

    public ActionResponse() {
    }

    public Coord getWorkerPosition() {
        return workerPosition;
    }

    public Coord getPosition() {
        return position;
    }
}
