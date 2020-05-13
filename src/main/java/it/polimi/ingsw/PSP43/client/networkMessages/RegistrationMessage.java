package it.polimi.ingsw.PSP43.client.networkMessages;

public class RegistrationMessage extends ClientMessage {
    private static final long SerialVersionUID=999888999111222333L;
    private String nick;

    /**
     * Not default constructor for RegistrationMessage
     * @param nick is the chosen nickname
     */
    public RegistrationMessage(String nick) {
        this.nick = nick;
    }

    /**
     * RegistrationMessage without parameters
     */
    public RegistrationMessage() {
    }

    /**
     * Getter method for the string nick
     * @return nick
     */
    public String getNick() {
        return nick;
    }
}
