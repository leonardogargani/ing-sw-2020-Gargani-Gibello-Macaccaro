package it.polimi.ingsw.PSP43.server.networkMessages;

public class ChangeNickRequest extends TextMessage {
    private static final long serialVersionUID = -4651206715194765707L;

    /**
     * Not default constructor for ChangeNickRequest message.
     * @param message is the string that will be shown to the recipient
     */
    public ChangeNickRequest(String message){super(message, TextMessage.PositionInScreen.LOW_CENTER);}


    /**
     * Getter method for the string message.
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
