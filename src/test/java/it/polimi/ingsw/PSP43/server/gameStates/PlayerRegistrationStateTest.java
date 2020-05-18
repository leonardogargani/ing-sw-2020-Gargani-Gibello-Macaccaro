package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
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
        assertEquals(2, spyGame.maxNumPlayers);
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