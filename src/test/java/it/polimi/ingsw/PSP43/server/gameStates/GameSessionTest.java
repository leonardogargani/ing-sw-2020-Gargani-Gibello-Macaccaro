package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.PlayersHandler;
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
    private GameSessionForTest spyGame;
    private ChooseCardState spyState;
    private String nickName;

    @Before
    public void setUp() throws Exception {
        GameSessionForTest gameSession = new GameSessionForTest(9);
        spyGame = spy(gameSession);
        ChooseCardState state = new ChooseCardState(gameSession);
        spyState = Mockito.spy(state);

        nickName = "Gibi";

        ClientListener clientMock = mock(ClientListener.class);
        doNothing().when(clientMock).sendMessage(any());

        TurnState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);

        doReturn(new PlayersNumberResponse(2)).when(spyGame).sendRequest(any(), any(), any());
        spyGame.registerToTheGame(new RegistrationMessage(nickName), clientMock);
        assertEquals(1, spyGame.getNumOfPlayers());
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
    public void eliminatePlayer() {
        Player playerEliminated = spyGame.getPlayersHandler().getPlayer(0);
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