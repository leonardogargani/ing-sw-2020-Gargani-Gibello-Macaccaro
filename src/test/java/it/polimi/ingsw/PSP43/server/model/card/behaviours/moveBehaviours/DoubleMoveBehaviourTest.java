package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DoubleMoveBehaviourTest {
    GameSession gameSession;
    GameSession spyGame;
    Player currentPlayer;
    AbstractGodCard abstractGodCard;

    @Before
    public void setUp() throws Exception {
        gameSession = GameInitialiser.initialiseGame();
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        spyGame = spy(gameSession);

        currentPlayer = gameSession.getPlayersHandler().getPlayer("Gibi");
        abstractGodCard = new BasicGodCard("", "", "", new DoubleMoveBehaviour(), new BasicBuildBehaviour());
        currentPlayer.setAbstractGodCard(abstractGodCard);
        spyGame.setCurrentPlayer(currentPlayer);
    }

    /**
     * This method tests if the two sub-sequent moves are done in the right way.
     */
    @Test
    public void handleMove() throws GameEndedException, WinnerCaughtException, GameLostException {
        Coord initialWorkerCoord = new Coord(4,3);
        Worker workerToMoveTwice = gameSession.getWorkersHandler().getWorker(initialWorkerCoord);

        Coord firstMove = new Coord(3,3);
        Coord secondMove = new Coord(2, 3);

        doReturn(new ActionResponse(initialWorkerCoord, firstMove),
                new ResponseMessage(true),
                new ActionResponse(firstMove, secondMove)).when(spyGame).sendRequest(any(), any(), any());

        currentPlayer.getAbstractGodCard().initMove(spyGame);

        assertFalse(gameSession.getCellsHandler().getCell(initialWorkerCoord).getOccupiedByWorker());
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
    public void findAvailablePositionsToMove() {
        Coord initialWorkerCoord = new Coord(4,3);
        Worker workerToMoveTwice = gameSession.getWorkersHandler().getWorker(initialWorkerCoord);
        DoubleMoveBehaviour doubleMoveBehaviour = new DoubleMoveBehaviour();

        gameSession.getWorkersHandler().changePosition(workerToMoveTwice, new Coord(3,3));
        Worker updatedWorkerToMoveTwice = spyGame.getWorkersHandler().getWorker(workerToMoveTwice.getId());
        HashMap<Coord, ArrayList<Coord>> actualHashMap = doubleMoveBehaviour.findAvailablePositionsToMove(spyGame, initialWorkerCoord);

        for (Coord keyCoord : actualHashMap.keySet()) {
            ArrayList<Coord> positions = actualHashMap.get(keyCoord);
            for (Coord c : positions) assertNotEquals(c, updatedWorkerToMoveTwice.getPreviousPosition());
        }
    }
}