package it.polimi.ingsw.PSP43.server.model.card.behaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.DataToAction;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DoubleMoveBehaviourTest {
    GameSession gameSession;
    GameSession spyGame;
    ArrayList<AbstractGodCard> deck;
    BasicGodCard artemis;
    DoubleMoveBehaviour doubleMoveBehaviour;
    DoubleMoveBehaviour spyMoveBehaviour;
    Worker workerToMoveTwice;
    Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = spy(gameSession);

        deck = DOMCardsParser.buildDeck();
        doubleMoveBehaviour = new DoubleMoveBehaviour();
        spyMoveBehaviour = spy(doubleMoveBehaviour);
        artemis = new BasicGodCard("", "", "", spyMoveBehaviour, null);

        currentPlayer = gameSession.getPlayersHandler().getPlayer("Gibi");

        int[] workersIds = currentPlayer.getWorkersIdsArray();
        workerToMoveTwice = null;
        for (int workersId : workersIds) {
            Worker actualWorker = gameSession.getWorkersHandler().getWorker(workersId);
            if (actualWorker.getCurrentPosition().equals(new Coord(4, 3)))
                workerToMoveTwice = actualWorker;
        }
    }

    /**
     * This method tests if the two sub-sequent moves are done in the right way.
     */
    @Test
    public void handleMove() throws ClassNotFoundException, GameEndedException, InterruptedException, IOException, WinnerCaughtException {
        assert workerToMoveTwice != null;
        Coord initialPosition = workerToMoveTwice.getCurrentPosition();
        Coord firstMove = new Coord(3,3);
        Coord secondMove = new Coord(2, 3);

        doReturn(null).when(spyMoveBehaviour).findAvailablePositionsToMove(any(), any(), any());
        doReturn(new ResponseMessage(true),
                new ActionResponse(firstMove, secondMove)).when(spyGame).sendRequest(any(), any(), any());

        artemis.move(new DataToAction(spyGame, currentPlayer, workerToMoveTwice, firstMove));

        assertFalse(gameSession.getCellsHandler().getCell(initialPosition).getOccupiedByWorker());
        assertFalse(gameSession.getCellsHandler().getCell(firstMove).getOccupiedByWorker());
        assertTrue(gameSession.getCellsHandler().getCell(secondMove).getOccupiedByWorker());
        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        assertEquals(workersHandler.getWorker(workerToMoveTwice.getId()).getCurrentPosition(), secondMove);
        assertEquals(workersHandler.getWorker(workerToMoveTwice.getId()).getPreviousPosition(), firstMove);
    }

    /**
     * This method tests that it is not possible for a player to move his worker back to the previous position.
     */
    @Test
    public void findAvailablePositionsToMove() throws IOException {
        ArrayList<Worker> workers = new ArrayList<>();
        workers.add(workerToMoveTwice);

        gameSession.getWorkersHandler().changePosition(workerToMoveTwice, new Coord(3,3));
        Worker updatedWorkerToMoveTwice = spyGame.getWorkersHandler().getWorker(workerToMoveTwice.getId());
        HashMap<Coord, ArrayList<Coord>> actualHashMap = spyMoveBehaviour.findAvailablePositionsToMove(spyGame.getCellsHandler(), workers, updatedWorkerToMoveTwice.getPreviousPosition());

        for (Coord keyCoord : actualHashMap.keySet()) {
            ArrayList<Coord> positions = actualHashMap.get(keyCoord);
            for (Coord c : positions) assertFalse(c.equals(updatedWorkerToMoveTwice.getPreviousPosition()));
        }
    }
}