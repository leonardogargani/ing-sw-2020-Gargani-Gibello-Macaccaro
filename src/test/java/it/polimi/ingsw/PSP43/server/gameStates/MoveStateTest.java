package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
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
import static org.mockito.Mockito.*;

public class MoveStateTest {
    GameSession gameSession;
    GameSession spyGame;
    BoardObserver obs;
    BoardObserver spyObs;
    ArrayList<AbstractGodCard> deck;
    MoveState state;
    MoveState spyState;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = Mockito.spy(gameSession);
        obs = new BoardObserver(gameSession);
        spyObs = Mockito.spy(obs);
        deck = DOMCardsParser.buildDeck();
        state = new MoveState(spyGame);
        spyState = Mockito.spy(state);
    }

    @Test
    public void initStateFirstExecution() throws IOException, ClassNotFoundException, InterruptedException, WinnerCaughtException {
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
    public void initState() throws IOException, ClassNotFoundException, InterruptedException, WinnerCaughtException {
        doNothing().when(spyGame).sendBroadCast(any());
        doNothing().when(spyState).executeState();
        spyState.initFirst = 0;
        spyGame.setCurrentPlayer(gameSession.getPlayersHandler().getPlayer(0));
        String precPlayerNick = spyGame.getCurrentPlayer().getNickname();

        spyState.initState();

        PlayersHandler playersHandler = spyGame.getPlayersHandler();
        String currentPlayerNick = spyGame.getCurrentPlayer().getNickname();

        int precPlayerNickPosition = 0;
        int currentPlayerNickPosition = 0;

        for (int i = 0; i < playersHandler.getNumOfPlayers(); i++) {
            if (currentPlayerNick.equals(playersHandler.getPlayer(i).getNickname())) {
                currentPlayerNickPosition = i;
            }
        }

        for (int i = 0; i < playersHandler.getNumOfPlayers(); i++) {
            if (precPlayerNick.equals(playersHandler.getPlayer(i).getNickname())) {
                precPlayerNickPosition = i;
            }
        }

        assertEquals(currentPlayerNickPosition, precPlayerNickPosition + 1);
    }

    @Test
    public void executeState() throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException, GameEndedException {
        // TODO : change a bit the configuration of the MoveState, removing some responsabilities

        AbstractGodCard mockCard = mock(AbstractGodCard.class);
        doNothing().when(mockCard).move(any());
        doReturn(new ActionResponse(new Coord(4, 3), new Coord(3, 3))).when(spyGame).sendRequest(any(), any(), any());
        doNothing().when(spyState).findNextState();
        Player currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        WorkersHandler workersHandler = spyGame.getWorkersHandler();
        Integer[] playerWorkers = spyGame.getPlayersHandler().getPlayer(currentPlayer.getNickname()).getWorkersIdsArray();

        Coord coordWorkerMoved = workersHandler.getWorker(playerWorkers[0]).getCurrentPosition();


        spyState.executeState();
    }

    @Test
    public void findNextState() {
    }
}