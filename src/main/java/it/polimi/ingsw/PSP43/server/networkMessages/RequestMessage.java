package it.polimi.ingsw.PSP43.server.networkMessages;


public class RequestMessage extends TextMessage {

    private static final long serialVersionUID = 1789377735218324041L;


    /**
     * Not default constructor for RequestMessage.
     * @param message is the string that will be shown to the recipient
     */
    public RequestMessage(String message){super(message, TextMessage.PositionInScreen.LOW_CENTER);}


    /**
     * Getter method for the string message.
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
