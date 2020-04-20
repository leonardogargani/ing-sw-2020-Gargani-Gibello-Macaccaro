package it.polimi.ingsw.PSP43.client.networkMessages;

public class NumberPlayerResponse extends ClientMessage {
    private static final long SerialVersionUID=111112222233333444L;
    private int numberOfPlayer;

    public NumberPlayerResponse(int numberOfPlayer){
        this.numberOfPlayer=numberOfPlayer;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }
}
