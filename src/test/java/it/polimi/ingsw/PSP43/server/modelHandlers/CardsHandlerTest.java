package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class CardsHandlerTest {
    private ArrayList<AbstractGodCard> deckComplete;
    private CardsHandler cardsHandlerIn;

    @Before
    public void setUp() throws Exception {
        cardsHandlerIn = new CardsHandler();
    }

    @Test
    public void getDeckOfAbstractGodCards() {
        cardsHandlerIn.print();
    }

    @Test
    public void getMapOwnerCard() {
    }

    @Test
    public void setCardToPlayer() {
    }

    @Test
    public void removeCardToPlayer() {
    }

    @Test
    public void getCardOwned() {
    }
}