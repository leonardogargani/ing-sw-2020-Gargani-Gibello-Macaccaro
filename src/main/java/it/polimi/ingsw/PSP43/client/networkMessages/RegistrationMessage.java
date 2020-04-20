package it.polimi.ingsw.PSP43.client.networkMessages;

public class RegistrationMessage extends ClientMessage {
    private static final long SerialVersionUID=999888999111222333L;
    private String nick;

    public RegistrationMessage(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
}
