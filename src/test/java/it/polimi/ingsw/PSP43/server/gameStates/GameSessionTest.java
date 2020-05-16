package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.RegisterClientListener;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class GameSessionTest {
    private GameSession gameSession;
    private GameSession spyGame;
    private ChooseCardState state;
    private ChooseCardState spyState;
    private ChooseCardState mockState;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSession(9);
        spyGame = spy(gameSession);
        state = new ChooseCardState(gameSession);
        spyState = spy(state);
    }

    @Test
    public void runTest() throws InterruptedException, IOException, ClassNotFoundException {
        Thread gameSessionThread = new Thread(spyGame);
        gameSessionThread.start();

        doNothing().when(spyState).initState();

        spyGame.setNextState(spyState);

        TimeUnit.SECONDS.sleep(2);
        assertTrue(spyGame.getCurrentState() instanceof ChooseCardState);

        spyGame.setActive();
    }

    @Test
    public void getActive() {
    }

    @Test
    public void setActive() {
    }

    @Test
    public void transitToNextState() {
    }

    @Test
    public void eliminatePlayer() {
    }

    @Test
    public void getCurrentState() {
    }

    @Test
    public void setCurrentState() {
    }

    @Test
    public void getNextState() {
    }

    @Test
    public void setNextState() {
    }

    @Test
    public void getTurnMap() {
    }

    @Test
    public void getCurrentPlayer() {
    }

    @Test
    public void setCurrentPlayer() {
    }

    @Test
    public void getCellsHandler() {
    }

    @Test
    public void getPlayersHandler() {
    }

    @Test
    public void getWorkersHandler() {
    }

    @Test
    public void getCardsHandler() {
    }

    @Test
    public void getBoardObserver() {
    }

    @Test
    public void unregisterFromGame() {
    }

    @Test
    public void sendEndingMessage() {
    }
}