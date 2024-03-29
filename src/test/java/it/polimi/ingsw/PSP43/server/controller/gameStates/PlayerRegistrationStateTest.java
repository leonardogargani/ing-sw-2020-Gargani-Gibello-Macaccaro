package it.polimi.ingsw.PSP43.server.controller.gameStates;

import it.polimi.ingsw.PSP43.client.network.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.model.BoardObserver;
import it.polimi.ingsw.PSP43.server.model.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.NicknameAlreadyInUseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class PlayerRegistrationStateTest {
    GameSession gameSession;
    GameSession spyGame;
    BoardObserver obs;
    BoardObserver spyObs;
    ArrayList<AbstractGodCard> deck;
    PlayerRegistrationState state;
    PlayerRegistrationState spyState;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException {
        gameSession = GameInitialiser.initialiseGame();
        spyGame = Mockito.spy(gameSession);
        obs = new BoardObserver(spyGame);
        spyObs = Mockito.spy(obs);
        deck = DOMCardsParser.buildDeck();
        state = new PlayerRegistrationState(spyGame);
        spyState = Mockito.spy(state);
    }

    @Test
    public void executeStateTest() throws GameEndedException {
        Mockito.doReturn(new PlayersNumberResponse(2)).when(spyGame).sendRequest(any(), any(), any());
        Mockito.doNothing().when(spyGame).sendMessage(any(), any());
        Mockito.doNothing().when(spyState).findNextState();

        assertEquals(1, spyGame.maxNumPlayers);
        spyState.executeState(new RegistrationMessage("Gibi"));
        assertTrue(spyGame.getPlayersHandler().getNumOfPlayers() == 1 && spyGame.getPlayersHandler().getPlayer("Gibi")!=null);
        assertEquals(2, spyGame.getMaxNumPlayers());
        spyState.executeState(new RegistrationMessage("Rob"));
        assertTrue(spyGame.getPlayersHandler().getNumOfPlayers() == 2 && spyGame.getPlayersHandler().getPlayer("Rob")!=null);
    }

    @Test
    public void findNextState() {
        Mockito.doNothing().when(spyGame).transitToNextState();

        spyState.findNextState();
        assertTrue(spyGame.getNextState() instanceof ChooseCardState);
    }
}