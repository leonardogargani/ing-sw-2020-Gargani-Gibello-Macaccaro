package it.polimi.ingsw.PSP43.server.networkMessages;


public class PlayersNumberRequest extends TextMessage {

    private static final long serialVersionUID = 1781439982170387947L;


    /**
     * Not default constructor for PlayerNumberRequest message.
     * @param message is the string that will be shown to the recipient
     */
    public PlayersNumberRequest(String message){super(message);}


    /**
     * Getter method for the string message.
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
