package it.polimi.ingsw.PSP43.client.networkMessages;

public class ChoseCardMessage extends ClientMessage {
    private static final long SerialVersionUID=321098765432109876L;
    private String cardName;

    public ChoseCardMessage(String cardName){
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }
}
