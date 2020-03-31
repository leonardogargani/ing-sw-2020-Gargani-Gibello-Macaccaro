package it.polimi.ingsw.model;

import it.polimi.ingsw.model.GameSession;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerDescriptorTest {

    private GameSession actualGame;
    private Player actualPlayer;
    private boolean actualQuit;
    private String actualNick;
    private Card actualCard;
    private PlayerDescriptor test;

    @Before
    public void setUp(){
        actualGame = new GameSession(9);
        actualPlayer = new Player("Gibi");
        actualQuit = false;
        actualCard = new Card("Hermes", "foo description");
        test = new PlayerDescriptor(actualGame, actualPlayer, actualQuit, actualNick, actualCard);
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
        assertEquals(actualCard, test.getCard());
    }
}