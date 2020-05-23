package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours.BasicMoveBehaviour;
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

    @Test
    public void setCardToPlayer() {
        String actualNickname = "Gibi";
        AbstractGodCard actualAbstractGodCard = new BasicGodCard("Atlas", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour());

        handlerTest.createNewPlayer(actualNickname);
        Player actualPlayer = handlerTest.getPlayer(actualNickname);
        handlerTest.setCardToPlayer(actualPlayer, actualAbstractGodCard);

        boolean equals = false;
        if (actualPlayer.getAbstractGodCard().getGodName().equals(actualAbstractGodCard.getGodName()) && actualPlayer.getAbstractGodCard().getDescription().equals(actualAbstractGodCard.getDescription()))
            equals = true;
        assertTrue(equals);
    }
}