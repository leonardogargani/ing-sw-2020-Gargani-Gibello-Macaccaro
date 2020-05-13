package it.polimi.ingsw.PSP43.server.networkMessages;

public class StartGameMessage extends TextMessage {
    /**
     * Not default constructor for StartGameMessage
     * @param message is the string that will be shown to the recipient
     */
    public StartGameMessage(String message) {
        super(message);
    }

    /**
     * Getter method for the string message
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
