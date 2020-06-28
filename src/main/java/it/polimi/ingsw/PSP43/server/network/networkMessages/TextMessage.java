package it.polimi.ingsw.PSP43.server.network.networkMessages;


public class TextMessage extends ServerMessage {

    private static final long serialVersionUID = -1927471145939939970L;
    private final String message;

    public enum PositionInScreen {
        HIGH_CENTER,
        LOW_CENTER;
    }

    private final PositionInScreen positionInScreen;

    /**
     * Not default constructor for TextMessage.
     * @param message It is the string that will be shown to the recipient
     * @param positionInScreen The position in which has to be rendered the message.
     */
    public TextMessage(String message, PositionInScreen positionInScreen){
        this.positionInScreen = positionInScreen;
        this.message = message;
    }


    /**
     * Getter method for the string message.
     * @return message
     */
    public String getMessage() {
        return message;
    }

    public PositionInScreen getPositionInScreen() {
        return positionInScreen;
    }
}
