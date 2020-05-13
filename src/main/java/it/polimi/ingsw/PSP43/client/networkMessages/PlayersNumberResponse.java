package it.polimi.ingsw.PSP43.client.networkMessages;

public class PlayersNumberResponse extends ClientMessage {
    private static final long SerialVersionUID=111112222233333444L;
    private int numberOfPlayer;

    /**
     * Not default constructor for PlayersNumberResponse message
     * @param numberOfPlayer is the chosen number of player for this game
     */
    public PlayersNumberResponse(int numberOfPlayer){
        this.numberOfPlayer=numberOfPlayer;
    }

    /**
     * PlayersNumberResponse constructor without parameters
     */
    public PlayersNumberResponse() {
    }

    /**
     * Getter method for the integer variable numberOfPlayer
     * @return numberOfPlayer
     */
    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }
}
