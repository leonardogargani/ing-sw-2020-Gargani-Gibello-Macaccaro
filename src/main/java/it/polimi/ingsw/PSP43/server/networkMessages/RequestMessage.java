package it.polimi.ingsw.PSP43.server.networkMessages;

public class RequestMessage extends TextMessage {
    private static final long SerialVersionUID=899899899899899899L;

    /**
     * Not default constructor for RequestMessage
     * @param message is the string that will be shown to the recipient
     */
    public RequestMessage(String message){super(message);}

    /**
     * Getter method for the string message
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
