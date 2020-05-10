package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.ArrayList;

public class ChosenCardsResponse extends ClientMessage {
    private static final long SerialVersionUID=321098765432109876L;
    private ArrayList<AbstractGodCard> cards;

    public ChosenCardsResponse(ArrayList<AbstractGodCard> cards){
        this.cards = cards;
    }

    public ChosenCardsResponse() {
    }

    public ArrayList<AbstractGodCard> getCardsName() {
        return cards;
    }
}
