package it.polimi.ingsw.PSP43.client.networkMessages;

public class LeaveGameMessage extends ReqMessage {
    private static final long SerialVersionUID=654321098765432109L;

    public LeaveGameMessage(String nick){
        super(nick);
    }

    @Override
    public String getNick() {
        return super.getNick();
    }
}
