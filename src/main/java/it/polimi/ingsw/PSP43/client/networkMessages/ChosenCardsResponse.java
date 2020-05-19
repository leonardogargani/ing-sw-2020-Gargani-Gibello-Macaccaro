package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.ArrayList;


public class ChosenCardsResponse extends ClientMessage {

    private static final long SerialVersionUID=321098765432109876L;
    private ArrayList<AbstractGodCard> cards;


    /**
     * Not default constructor for ChosenCardResponse message.
     * @param cards is an ArrayList of AbstractGodCard chosen from the game creator player
     */
    public ChosenCardsResponse(ArrayList<AbstractGodCard> cards){
        this.cards = cards;
    }


    /**
     * ChosenCardsResponse constructor without parameters.
     */
    public ChosenCardsResponse() {
    }


    /**
     * Getter method for the ArrayList cards.
     * @return cards
     */
    public ArrayList<AbstractGodCard> getCardsName() {
        return cards;
    }

}
