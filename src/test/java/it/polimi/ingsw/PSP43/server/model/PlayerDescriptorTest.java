package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerDescriptorTest {

    private GameSession actualGame;
    private Player actualPlayer;
    private boolean actualQuit;
    private String actualNick;
    private AbstractGodCard actualAbstractGodCard;
    private PlayerDescriptor test;

    @Before
    public void setUp(){
        actualGame = new GameSession(9);
        actualPlayer = new Player("Gibi");
        actualQuit = false;
        actualAbstractGodCard = new BasicGodCard("Hermes", "foo description", null);
        test = new PlayerDescriptor(actualGame, actualPlayer, actualQuit, actualNick, actualAbstractGodCard);
    }

    @Test
    public void getGameIdentifier() {
        assertEquals(actualGame, test.getGameIdentifier());
    }

    @Test
    public void getPlayerReference() {
        assertEquals(actualPlayer, test.getPlayerReference());
    }

    @Test
    public void isQuitting() {
        assertTrue(actualQuit == test.isQuitting());
    }

    @Test
    public void getNick() {
        assertEquals(actualNick, test.getNick());
    }

    @Test
    public void getCard() {
        assertEquals(actualAbstractGodCard, test.getCard());
    }
}