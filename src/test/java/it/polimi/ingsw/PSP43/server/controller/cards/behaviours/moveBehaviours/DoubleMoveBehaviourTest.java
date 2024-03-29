package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DoubleMoveBehaviourTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
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
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
        spyGame.setCurrentPlayer(currentPlayer);
    }

    /**
     * This method tests if the two sub-sequent moves are done in the right way.
     */
    @Test
    public void handleMove() throws GameEndedException, GameLostException {
        Coord initialWorkerCoord = new Coord(4,3);
        Worker workerToMoveTwice = gameSession.getWorkersHandler().getWorker(initialWorkerCoord);

        Coord firstMove = new Coord(3,3);
        Coord secondMove = new Coord(2, 3);

        doReturn(new ActionResponse(initialWorkerCoord, firstMove),
                new ResponseMessage(true),
                new ActionResponse(firstMove, secondMove)).when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initMove(spyGame);

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
        HashMap<Coord, ArrayList<Coord>> actualHashMap =
                doubleMoveBehaviour.findAvailablePositionsToMove(spyGame, initialWorkerCoord, workerToMoveTwice.getCurrentPosition());

        for (Coord keyCoord : actualHashMap.keySet()) {
            ArrayList<Coord> positions = actualHashMap.get(keyCoord);
            assertEquals(keyCoord, workerToMoveTwice.getCurrentPosition());
            for (Coord c : positions) assertNotEquals(c, updatedWorkerToMoveTwice.getPreviousPosition());
        }
    }
}