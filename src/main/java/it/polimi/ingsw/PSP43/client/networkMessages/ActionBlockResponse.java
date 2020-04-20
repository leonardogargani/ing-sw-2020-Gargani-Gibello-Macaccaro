package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

public class ActionBlockResponse extends ActionResponse {
    private static final long SerialVersionUID=342342342342342342L;

    public ActionBlockResponse(int idWorker, Coord position){super(idWorker, position);}
}
