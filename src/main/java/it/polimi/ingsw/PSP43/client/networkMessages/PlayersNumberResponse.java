package it.polimi.ingsw.PSP43.client.networkMessages;

public class PlayersNumberResponse extends ClientMessage {
    private static final long SerialVersionUID=111112222233333444L;
    private int numberOfPlayer;

    public PlayersNumberResponse(int numberOfPlayer){
        this.numberOfPlayer=numberOfPlayer;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }
}
