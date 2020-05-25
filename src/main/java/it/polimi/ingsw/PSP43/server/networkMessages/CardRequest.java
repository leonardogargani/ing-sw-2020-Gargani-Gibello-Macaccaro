package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import java.util.List;


public class CardRequest extends ServerMessage {

    private static final long serialVersionUID = -1627227471394889344L;
    private final List<AbstractGodCard> cards;


    /**
     * Not default constructor for CardRequest message.
     * @param cards is a List of AbstractGodCard among which the recipient will have to choose
     */
    public CardRequest(List<AbstractGodCard> cards){
        this.cards=cards;
    }


    /**
     * Getter method for the List cards.
     * @return cards
     */
    public List<AbstractGodCard> getCards() {
        return cards;
    }

}
