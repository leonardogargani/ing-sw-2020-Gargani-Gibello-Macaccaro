package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

import java.util.ArrayList;

public class InitialCardsRequest extends TextMessage {
    private static final long SerialVersionUID=757473757473737473L;
    private ArrayList<AbstractGodCard> cards;
    private int numberOfCard;

    /**
     * Not default constructor for InitialCardsRequest message
     * @param message is the string that will be shown to the recipient
     * @param cards is an ArrayList of AbstractGodCard among which the creator of the game will have to choose and
     * these chosen cards will be those present in the game
     * @param numberOfCard is the number of cards that the first player must choose
     */
    public InitialCardsRequest(String message,ArrayList<AbstractGodCard> cards,int numberOfCard){
        super(message);
        this.cards = cards;
        this.numberOfCard = numberOfCard;
    }

    /**
     * Getter method for the integer variable numberOfCard
     * @return numberOfCard
     */
    public int getNumberOfCard() {
        return numberOfCard;
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
