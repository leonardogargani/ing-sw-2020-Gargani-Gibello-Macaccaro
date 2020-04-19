package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

public class ActionRequest extends ReqMessage {
    private static final long SerialVersionUID=543210987654321098L;
    private int idWorker;
    private Coord position;

    public ActionRequest(String nick,int idWorker,Coord position){
        super(nick);
        this.idWorker = idWorker;
        this.position = position;
    }

    public int getIdWorker() {
        return idWorker;
    }

    public Coord getPosition() {
        return position;
    }

    @Override
    public String getNick() {
        return super.getNick();
    }
}
