package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.List;

public class CardRequest extends TextMessage {
    private static final long serialVersionUID = -1627227471394889344L;
    private final List<AbstractGodCard> cards;

    /**
     * Not default constructor for CardRequest message
     * @param message is the string that will be shown to the recipient
     * @param cards is an ArrayList of AbstractGodCard among which the recipient will have to choose
     */
    public CardRequest(String message, List<AbstractGodCard> cards){
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
    public List<AbstractGodCard> getCards() {
        return cards;
    }
}
