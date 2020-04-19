package it.polimi.ingsw.PSP43.client.networkMessages;

public class PingMessage extends ReqMessage {
    private static final long SerialVersionUID=765432109876543210L;

    public PingMessage(String nick){super(nick);}

    @Override
    public String getNick() {
        return super.getNick();
    }
}
