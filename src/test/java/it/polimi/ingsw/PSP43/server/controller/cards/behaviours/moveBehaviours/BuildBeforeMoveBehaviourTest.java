package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.client.network.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.network.networkMessages.ResponseMessage;
import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.WinnerCaughtException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BuildBeforeMoveBehaviourTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    ArrayList<AbstractGodCard> deck;
    Player currentPlayer;
    AbstractGodCard abstractGodCard;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSessionForTest(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);
        deck = DOMCardsParser.buildDeck();

        currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new BasicGodCard("", "", "", new BuildBeforeMoveBehaviour(), new BasicBuildBehaviour());
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    /**
     * This test is made to check the private method "buildBeforeMove(DataToMove oldData)" of the class,
     * which is the one that has to give the opportunity at runtime to the player to build before moving (here
     * tested the build of a block),
     * respecting certain conditions (no build in before-move position of the worker and no build in after-move
     * position of the worker). The method doesn't check if the move is successful because it is
     * already checked in the superclass method "move(...)".
     */
    @Test
    public void handleInitMoveWithBuildBlock() throws WinnerCaughtException, GameEndedException, GameLostException {
        Integer[] playerWorkers = currentPlayer.getWorkersIdsArray();
        Worker workerToBuild = null;
        for (Integer playerWorker : playerWorkers) {
            workerToBuild = spyGame.getWorkersHandler().getWorker(playerWorker);
            if (workerToBuild.getCurrentPosition().equals(new Coord(4, 3))) break;
        }

        assert workerToBuild != null;
        doReturn(new ActionResponse(workerToBuild.getCurrentPosition(), new Coord(3, 2)),
                new ResponseMessage(true),
                new ActionResponse(workerToBuild.getCurrentPosition(), new Coord(3, 3))).when(spyGame).sendRequest(any(), any(), any());

        abstractGodCard.initMove(spyGame);

        assertEquals(1, spyGame.getCellsHandler().getCell(new Coord(3, 3)).getHeight());
    }

    /**
     * This test is made to check the private method "buildBeforeMove(DataToMove oldData)" of the class,
     * which is the one that has to give the opportunity at runtime to the player to build before moving (here
     * tested the build of a dome),
     * respecting certain conditions (no build in before-move position of the worker and no build in after-move
     * position of the worker). The method doesn't check if the move is successful because it is
     * already checked in the superclass method "move(...)".
     */
    @Test
    public void handleInitMoveWithBuildDome() throws WinnerCaughtException, GameEndedException, GameLostException {
        Player currentPlayer = spyGame.getPlayersHandler().getPlayer(0);
        Integer[] playerWorkers = currentPlayer.getWorkersIdsArray();
        Worker workerToBuild = null;
        for (Integer playerWorker : playerWorkers) {
            workerToBuild = spyGame.getWorkersHandler().getWorker(playerWorker);
            if (workerToBuild.getCurrentPosition().equals(new Coord(4, 3))) break;
        }

        Coord newThreeLevelCoord = new Coord(3, 3);
        Cell newThreeLevelCell = new Cell(newThreeLevelCoord, gameSession.getBoardObserver());
        newThreeLevelCell.setHeight(3);
        gameSession.getCellsHandler().changeStateOfCell(newThreeLevelCell, newThreeLevelCoord);

        assert workerToBuild != null;
        doReturn(new ActionResponse(workerToBuild.getCurrentPosition(), new Coord(3, 2)),
                new ResponseMessage(true), new ResponseMessage(true),
                new ActionResponse(workerToBuild.getCurrentPosition(), new Coord(3, 3))).when(spyGame).sendRequest(any(), any(), any());

        BuildBeforeMoveBehaviour spyBehaviour = spy(new BuildBeforeMoveBehaviour());
        AbstractGodCard spyCard = spy(new BasicGodCard("", "", "", spyBehaviour, new BasicBuildBehaviour()));

        doReturn(new HashMap<Coord, ArrayList<Coord>>()).when(spyBehaviour).findAvailablePositionsToBuildBlock(any(), any());
        doReturn(new HashMap<Coord, ArrayList<Coord>>()).when(spyBehaviour).findAvailablePositionsToMove(any());

        assert spyCard != null;
        spyCard.initMove(spyGame);

        assertTrue(spyGame.getCellsHandler().getCell(new Coord(3, 3)).getOccupiedByDome());
    }

    @Test
    public void findAvailablePositionsToBuildBlockOrDome() {
        Worker worker = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        Coord coordWhereToMove = new Coord(4, 4);
        DataToMove dataToMove = new DataToMove(spyGame, currentPlayer, worker, coordWhereToMove);

        BuildBeforeMoveBehaviour buildBeforeMoveBehaviour = new BuildBeforeMoveBehaviour();
        HashMap<Coord, ArrayList<Coord>> hashMap = buildBeforeMoveBehaviour.findAvailablePositionsToBuildBlock(spyGame, dataToMove);

        for (Coord key : hashMap.keySet()) {
            ArrayList<Coord> positions = hashMap.get(key);
            assertFalse(positions.contains(new Coord(4, 4)));
            assertFalse(positions.contains(new Coord(4, 3)));
        }

        Coord coord = new Coord(3, 4);
        Cell cell = spyGame.getCellsHandler().getCell(coord);
        cell.setHeight(3);
        spyGame.getCellsHandler().changeStateOfCell(cell, coord);

        hashMap = buildBeforeMoveBehaviour.findAvailablePositionsToBuildDome(spyGame, dataToMove);

        for (Coord key : hashMap.keySet()) {
            ArrayList<Coord> positions = hashMap.get(key);
            assertFalse(positions.contains(new Coord(4, 4)));
            assertFalse(positions.contains(new Coord(4, 3)));
        }
    }

    @Test
    public void findIfNotAvailablePositionsTest() {
        Worker worker = spyGame.getWorkersHandler().getWorker(new Coord(4, 3));
        Coord coordWhereToMove = new Coord(4, 4);
        DataToMove dataToMove = new DataToMove(spyGame, currentPlayer, worker, coordWhereToMove);

        ArrayList<Coord> neighbouring = spyGame.getCellsHandler().findNeighbouringCoords(worker.getCurrentPosition());
        for (Coord actualCoord : neighbouring) {
            spyGame.getCellsHandler().getCell(actualCoord).setOccupiedByDome(true);
        }

        BuildBeforeMoveBehaviour buildBeforeMoveBehaviour = new BuildBeforeMoveBehaviour();
        HashMap<Coord, ArrayList<Coord>> hashMap = buildBeforeMoveBehaviour.findAvailablePositionsToBuildBlock(spyGame, dataToMove);

        for (Coord key : hashMap.keySet()) {
            ArrayList<Coord> positions = hashMap.get(key);
            assertNull(positions);
        }
    }
}