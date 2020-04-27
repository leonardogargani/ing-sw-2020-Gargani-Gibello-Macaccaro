package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.ArrayList;

public class CardRequest extends TextMessage {
    private static final long SerialVersionUID=123456789012345678L;
    private ArrayList<AbstractGodCard> cards;
    
    public CardRequest(String message, ArrayList<AbstractGodCard> cards){
        super(message);
        this.cards=cards;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ArrayList<AbstractGodCard> getCards() {
        return cards;
    }
}
