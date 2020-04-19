package it.polimi.ingsw.PSP43.client.networkMessages;

public class RegistrationMessage extends ReqMessage {
    private static final long SerialVersionUID=999888999111222333L;

    public RegistrationMessage(String nick) {
        super(nick);
    }

    @Override
    public String getNick() {
        return super.getNick();
    }
}
