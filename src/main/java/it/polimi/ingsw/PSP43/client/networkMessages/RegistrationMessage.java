package it.polimi.ingsw.PSP43.client.networkMessages;


public class RegistrationMessage extends ClientMessage {
    private static final long serialVersionUID = 8041591888628625078L;
    private String nick;


    /**
     * Not default constructor for RegistrationMessage.
     * @param nick is the chosen nickname
     */
    public RegistrationMessage(String nick) {
        this.nick = nick;
    }


    /**
     * RegistrationMessage without parameters.
     */
    public RegistrationMessage() {
    }


    /**
     * Getter method for the string nick.
     * @return nick
     */
    public String getNick() {
        return nick;
    }

}
