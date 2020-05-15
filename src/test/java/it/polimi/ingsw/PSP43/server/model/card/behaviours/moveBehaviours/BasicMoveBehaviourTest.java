package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class BasicMoveBehaviourTest {
    GameSession gameSession;
    GameSession spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSession(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);
        deck = DOMCardsParser.buildDeck();

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);
    }

    @Test
    public void testAskForMove() throws ClassNotFoundException, GameEndedException, InterruptedException, IOException {
        currentPlayer.setAbstractGodCard(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour()));

        Worker workerToMove = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        Coord coordToMove = new Coord(3, 3);
        doReturn(new ActionResponse(workerToMove.getCurrentPosition(), coordToMove)). when(spyGame).sendRequest(any(),any(),any());

        ActionResponse actionResponse = currentPlayer.getAbstractGodCard().askForMove(spyGame);

        assertEquals(actionResponse.getPosition(), coordToMove);
        assertEquals(actionResponse.getWorkerPosition(), workerToMove.getCurrentPosition());
    }

    @Test
    public void testHandleInitMove() throws ClassNotFoundException, GameEndedException, InterruptedException, IOException, WinnerCaughtException {
        currentPlayer.setAbstractGodCard(new BasicGodCard("", "", "", new BasicMoveBehaviour(), new BasicBuildBehaviour()));

        Coord workerCoord = new Coord(4,3);
        Worker workerToMove = spyGame.getWorkersHandler().getWorker(workerCoord);
        Coord coordToMove = new Coord(3, 3);
        doReturn(new ActionResponse(workerToMove.getCurrentPosition(), coordToMove)). when(spyGame).sendRequest(any(),any(),any());

        currentPlayer.getAbstractGodCard().initMove(spyGame);

        assertEquals(workerToMove.getCurrentPosition(), coordToMove);
        assertEquals(workerToMove.getPreviousPosition(), workerCoord);
        assertFalse(spyGame.getCellsHandler().getCell(workerCoord).getOccupiedByWorker());
        assertTrue(spyGame.getCellsHandler().getCell(coordToMove).getOccupiedByWorker());
    }
}