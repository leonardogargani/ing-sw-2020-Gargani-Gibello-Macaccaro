package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MoveStateTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    MoveState state;
    MoveState spyState;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException, GameLostException, GameEndedException, WinnerCaughtException {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = Mockito.spy(gameSession);
        state = new MoveState(spyGame);
        spyState = Mockito.spy(state);
    }

    @Test
    public void initStateFirstExecution() {
        doNothing().when(spyGame).sendBroadCast(any());
        doNothing().when(spyState).executeState();

        spyState.initState();

        PlayersHandler playersHandler = spyGame.getPlayersHandler();
        Player godLikePlayer = playersHandler.getPlayer(0);
        assertEquals(spyGame.getCurrentPlayer().getNickname(), godLikePlayer.getNickname());

        WorkersHandler workersHandler = spyGame.getWorkersHandler();
        for (Worker w : workersHandler.getWorkers()) {
            assertFalse(w.isLatestMoved());
        }
    }

    @Test
    public void initStateTest() {
        doNothing().when(spyGame).sendBroadCast(any());
        doNothing().when(spyState).executeState();

        spyState.initFirst = 0;

        Player precPlayer = gameSession.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(precPlayer);
        Player nextPlayer = spyGame.getPlayersHandler().getNextPlayer(precPlayer.getNickname());

        spyState.initState();

        Player currentPlayer = spyGame.getCurrentPlayer();

        assertEquals(currentPlayer.getNickname(), nextPlayer.getNickname());
    }

    @Test
    public void executeTest() {
        Player currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        AbstractGodCard mockCard = mock(AbstractGodCard.class);
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), mockCard);

        spyGame.setCurrentPlayer(currentPlayer);
        spyState.executeState();
    }

    @Test
    public void findNextState() {
        spyState.findNextState();

        assertSame(spyGame.getNextState().getTurnName(), TurnState.TurnName.BUILD_STATE);
    }
}