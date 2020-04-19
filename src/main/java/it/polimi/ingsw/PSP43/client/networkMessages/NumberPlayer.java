package it.polimi.ingsw.PSP43.client.networkMessages;

public class NumberPlayer extends ReqMessage {
    private static final long SerialVersionUID=111112222233333444L;
    private int numberOfPlayer;

    public NumberPlayer(String nick, int numberOfPlayer){
        super(nick);
        this.numberOfPlayer=numberOfPlayer;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    @Override
    public String getNick() {
        return super.getNick();
    }
}
