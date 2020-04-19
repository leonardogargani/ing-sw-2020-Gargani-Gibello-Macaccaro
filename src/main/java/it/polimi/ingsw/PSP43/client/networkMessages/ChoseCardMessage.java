package it.polimi.ingsw.PSP43.client.networkMessages;

public class ChoseCardMessage extends ReqMessage {
    private static final long SerialVersionUID=321098765432109876L;
    private String cardName;

    public ChoseCardMessage(String nick,String cardName){
        super(nick);
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

    @Override
    public String getNick() {
        return super.getNick();
    }
}
