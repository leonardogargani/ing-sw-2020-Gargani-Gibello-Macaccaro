package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import java.util.List;


public class InitialCardsRequest extends ServerMessage {

    private static final long serialVersionUID = 300298265202994722L;
    private final List<AbstractGodCard> cards;
    private final int numberOfCard;


    /**
     * Not default constructor for InitialCardsRequest message.
     * @param cards is a List of AbstractGodCard among which the creator of the game will have to choose and
     * these chosen cards will be those present in the game
     * @param numberOfCard is the number of cards that the first player must choose
     */
    public InitialCardsRequest(List<AbstractGodCard> cards, int numberOfCard){
        this.cards = cards;
        this.numberOfCard = numberOfCard;
    }


    /**
     * Getter method for the integer variable numberOfCard.
     * @return numberOfCard
     */
    public int getNumberOfCard() {
        return numberOfCard;
    }


    /**
     * Getter method for the List cards.
     * @return cards
     */
    public List<AbstractGodCard> getCards() {
        return cards;
    }

}
