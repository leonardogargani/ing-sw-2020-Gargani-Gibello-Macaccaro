package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.ArrayList;

public class CardRequest extends TextMessage {
    private static final long SerialVersionUID=123456789012345678L;
    private ArrayList<AbstractGodCard> cards;

    /**
     * Not default constructor for CardRequest message
     * @param message is the string that will be shown to the recipient
     * @param cards is an ArrayList of AbstractGodCard among which the recipient will have to choose
     */
    public CardRequest(String message, ArrayList<AbstractGodCard> cards){
        super(message);
        this.cards=cards;
    }

    /**
     * Getter method for the string message
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * Getter method for the ArrayList cards
     * @return cards
     */
    public ArrayList<AbstractGodCard> getCards() {
        return cards;
    }
}
