package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BlockRiseFactory;
import it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class CardsHandlerTest {
    private CardsHandler cardsHandler;
    private final String firstNick = "Gibi";
    private final String firstGodName = "Athena";

    @Before
    public void setUp() throws Exception {
        cardsHandler = new CardsHandler();

    }

    @Test
    public void setCardToPlayerTest() {
        cardsHandler.setCardToPlayer(firstNick, firstGodName);

        AbstractGodCard abstractGodCard = cardsHandler.getMapOwnersCard().get(firstNick);
        assertEquals(abstractGodCard.getGodName(), firstGodName);
    }

    @Test
    public void removeCardToPlayerTest() {
        cardsHandler.setCardToPlayer(firstNick, firstGodName);
        cardsHandler.removeCardToPlayer(firstNick);

        AbstractGodCard abstractGodCard = cardsHandler.getMapOwnersCard().get(firstNick);
        assertNull(abstractGodCard);
    }

    @Test
    public void addAndRemoveDecoratorTest() {
        cardsHandler.setCardToPlayer(firstNick, firstGodName);
        String secondNick = "Rob";
        String secondGodName = "Atlas";
        cardsHandler.setCardToPlayer(secondNick, secondGodName);
        cardsHandler.addDecorator(firstGodName, new BlockRiseFactory());

        Map<String, AbstractGodCard> map = cardsHandler.getMapOwnersCard();
        for (String s : map.keySet()) {
            if (!(s.equals(firstNick))) {
                assertTrue(map.get(s) instanceof BlockRiseDecorator);
            }
        }

        cardsHandler.removeDecorator(firstGodName, "it.polimi.ingsw.PSP43.server.model.card.decorators.BlockRiseDecorator");

        map = cardsHandler.getMapOwnersCard();
        for (String s : map.keySet()) {
            if (!(s.equals(firstNick))) {
                assertFalse(map.get(s) instanceof BlockRiseDecorator);
            }
        }
    }
}