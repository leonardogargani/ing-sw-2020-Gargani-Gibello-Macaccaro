package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameSessionTest {
    private GameSession spyGame;
    private ChooseCardState spyState;
    private Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        GameSession gameSession = new GameSession(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);
        ChooseCardState state = new ChooseCardState(gameSession);
        spyState = Mockito.spy(state);

        spyGame.setCurrentPlayer(spyGame.getPlayersHandler().getPlayer(0));
        currentPlayer = spyGame.getCurrentPlayer();
    }

    @Test
    public void runTest() throws InterruptedException {
        Thread gameSessionThread = new Thread(spyGame);
        gameSessionThread.start();

        doNothing().when(spyState).initState();

        spyGame.setNextState(spyState);

        TimeUnit.SECONDS.sleep(3);
        assertTrue(spyGame.getCurrentState() instanceof ChooseCardState);

        spyGame.setActive();
        assertFalse(spyGame.getActive());
    }

    @Test
    public void eliminatePlayer() throws GameEndedException {
        ClientListener clientMock = mock(ClientListener.class);
        doNothing().when(clientMock).sendMessage(any());

        TurnState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        doReturn(new PlayersNumberResponse(2)).when(spyGame).sendRequest(any(), any(), any());
        spyGame.registerToTheGame(new RegistrationMessage(currentPlayer.getNickname()), clientMock);
        assertEquals(1, spyGame.getNumOfPlayers());

        Player playerEliminated = currentPlayer;
        Integer[] playersWorkerIds = playerEliminated.getWorkersIdsArray();

        spyGame.eliminatePlayer(playerEliminated);

        PlayersHandler playersHandler = spyGame.getPlayersHandler();
        assertNull(playersHandler.getPlayer(playerEliminated.getNickname()));

        for (Worker w : spyGame.getWorkersHandler().getWorkers()) {
            for (Integer i : playersWorkerIds)
                assertTrue(w.getId() != i);
        }

        Map<String, AbstractGodCard> mapCardsOwned = spyGame.getCardsHandler().getMapOwnersCard();
        AbstractGodCard playerAbstractGodCard = mapCardsOwned.get(playerEliminated.getNickname());
        assertNull(playerAbstractGodCard);
    }

    @Test
    public void unregisterFromGame() {
        spyGame.unregisterFromGame(new LeaveGameMessage(), new ClientListener(new Socket()));
    }

    @Test
    public void sendEndingMessage() {
        ArrayList<String> nicks = new ArrayList<>();
        nicks.add(spyGame.getPlayersHandler().getPlayer(0).getNickname());
        spyGame.sendEndingMessage(new EndGameMessage(null, EndGameMessage.EndGameHeader.LOSER), new EndGameMessage(null, EndGameMessage.EndGameHeader.WINNER), nicks);
    }
}