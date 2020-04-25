package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

public class ActionDomeResponse extends ActionResponse {
    private static final long SerialVersionUID=543543543543543543L;

    public ActionDomeResponse(Coord workerPosition, Coord position){super(workerPosition,position);}

    @Override
    public Coord getWorkerPosition() {
        return super.getWorkerPosition();
    }

    @Override
    public Coord getPosition() {
        return super.getPosition();
    }
}
