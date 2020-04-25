package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

public class ActionBlockResponse extends ActionResponse {
    private static final long SerialVersionUID=342342342342342342L;

    public ActionBlockResponse(Coord workerPosition, Coord position){super(workerPosition, position);}

    @Override
    public Coord getPosition() {
        return super.getPosition();
    }

    @Override
    public Coord getWorkerPosition() {
        return super.getWorkerPosition();
    }
}
