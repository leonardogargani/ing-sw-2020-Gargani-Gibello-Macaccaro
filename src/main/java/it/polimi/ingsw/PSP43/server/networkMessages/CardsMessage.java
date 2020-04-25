package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import javax.smartcardio.Card;
import java.util.ArrayList;

public class CardsMessage extends TextMessage {
    private static final long SerialVersionUID=123456789012345678L;
    private ArrayList<AbstractGodCard> cards;
    
    public CardsMessage(String message,ArrayList<AbstractGodCard> cards){
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
