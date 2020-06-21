package it.polimi.ingsw.PSP43.server.controller.modelHandlers;

import it.polimi.ingsw.PSP43.server.controller.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayersHandlerTest {
    PlayersHandler handlerTest;

    /**
     * This method initialises a set of players taking their nicknames from a csv file and the cards' data they own
     */
    @Before
    public void setUp() throws Exception {
        handlerTest = new PlayersHandler();
    }

    @Test
    public void createNewPlayer() {
        String actualNickname = "Nicolò";

        handlerTest.createNewPlayer(actualNickname);
        assertTrue(handlerTest.getPlayer(actualNickname).getNickname().equals(actualNickname));
    }

    @Test
    public void getPlayer() {
        String actualNickname = "Fabio";
        handlerTest.createNewPlayer(actualNickname);

        Player actualPlayer = handlerTest.getPlayer(actualNickname);
        assertTrue(actualPlayer != null && actualPlayer.getNickname().equals(actualNickname));
    }

    @Test
    public void deletePlayer() {
        String actualNickname = "Amilcare";

        handlerTest.createNewPlayer(actualNickname);

        handlerTest.deletePlayer(actualNickname);

        Player actualPlayer = handlerTest.getPlayer(actualNickname);
        assertNull(actualPlayer);
    }
}