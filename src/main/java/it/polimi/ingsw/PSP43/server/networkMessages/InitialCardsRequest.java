package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.ArrayList;

public class InitialCardsRequest extends TextMessage {
    private static final long SerialVersionUID=757473757473737473L;
    private ArrayList<AbstractGodCard> cards;
    private int numberOfCard;

    public InitialCardsRequest(String message,ArrayList<AbstractGodCard> cards,int numberOfCard){
        super(message);
        this.cards = cards;
        this.numberOfCard = numberOfCard;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ArrayList<AbstractGodCard> getCards() {
        return cards;
    }
}
