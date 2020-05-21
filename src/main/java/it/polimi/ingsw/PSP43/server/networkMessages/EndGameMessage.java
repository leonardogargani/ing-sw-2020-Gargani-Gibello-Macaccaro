package it.polimi.ingsw.PSP43.server.networkMessages;


public class EndGameMessage extends TextMessage {

    public enum EndGameHeader {
        WINNER,
        LOSER,
        DISCONNECTED;
    }

    private static final long serialVersionUID = 8430836759594606501L;

    private final EndGameHeader endGameHeader;

    /**
     * Not default constructor for EndGameMessage.
     * @param message is the string that will be shown to the recipient
     * @param endGameHeader
     */
    public EndGameMessage(String message, EndGameHeader endGameHeader){
        super(message);
        this.endGameHeader = endGameHeader;
    }


    /**
     * Getter method for the string message.
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }


    /**
     *
     * @return
     */
    public EndGameHeader getEndGameHeader() {
        return endGameHeader;
    }

}
