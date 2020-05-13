package it.polimi.ingsw.PSP43.server.networkMessages;

public class TextMessage extends ServerMessage {
    private static final long SerialVersionUID=111234567890123456L;
    private String message;

    /**
     * Not default constructor for TextMessage
     * @param message is the string that will be shown to the recipient
     */
    public TextMessage( String message){
        this.message = message;
    }

    /**
     * Getter method for the string message
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
