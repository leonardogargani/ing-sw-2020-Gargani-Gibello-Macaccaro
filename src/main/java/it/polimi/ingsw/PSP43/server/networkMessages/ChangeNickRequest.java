package it.polimi.ingsw.PSP43.server.networkMessages;

public class ChangeNickRequest extends TextMessage {
    private static final long SerialVersionUID=789789789789789789L;

    /**
     * Not default constructor for ChangeNickRequest message
     * @param message is the string that will be shown to the recipient
     */
    public ChangeNickRequest(String message){super(message);}

    /**
     * Getter method for the string message
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
