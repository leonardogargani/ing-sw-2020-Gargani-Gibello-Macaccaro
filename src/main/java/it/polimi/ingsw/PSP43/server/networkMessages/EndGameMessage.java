package it.polimi.ingsw.PSP43.server.networkMessages;

public class EndGameMessage extends TextMessage {
    private static final long serialVersionUID = 8430836759594606501L;

    /**
     * Not default constructor for EndGameMessage
     * @param message is the string that will be shown to the recipient
     */
    public EndGameMessage(String message){super(message);}

    /**
     * Getter method for the string message
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
