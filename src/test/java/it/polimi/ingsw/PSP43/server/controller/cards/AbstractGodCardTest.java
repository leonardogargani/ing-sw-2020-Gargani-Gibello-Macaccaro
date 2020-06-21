package it.polimi.ingsw.PSP43.server.controller.cards;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionForTest;
import it.polimi.ingsw.PSP43.server.model.initialisers.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.CellsHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class AbstractGodCardTest {
    GameSessionForTest gameSession;
    GameSessionForTest spyGame;
    AbstractGodCard abstractGodCard;
    Player currentPlayer;

    @Before
    public void setUp() throws Exception {
        gameSession = new GameSessionForTest(9);
        GameInitialiser.initialisePlayers(gameSession);
        GameInitialiser.initialiseWorkers(gameSession);
        GameInitialiser.initialiseCards(gameSession);
        spyGame = spy(gameSession);

        currentPlayer = spyGame.getPlayersHandler().getPlayer("Gibi");
        spyGame.setCurrentPlayer(currentPlayer);

        abstractGodCard = new BasicGodCard();
        spyGame.getCardsHandler().setCardToPlayer(currentPlayer.getNickname(), abstractGodCard);
    }

    @Test
    public void moveTest() {
        Worker workerToMove = spyGame.getWorkersHandler().getWorker(new Coord(4,3));
        Coord initialCoord = workerToMove.getCurrentPosition();
        Coord coordToMove = new Coord(3,3);

        // TODO : is the player necessary in DAO DataToMove?
        abstractGodCard.move(new DataToMove(spyGame, null, workerToMove, coordToMove));

        assertTrue(spyGame.getCellsHandler().getCell(coordToMove).getOccupiedByWorker());
        assertFalse(spyGame.getCellsHandler().getCell(initialCoord).getOccupiedByWorker());
        assertEquals(workerToMove.getCurrentPosition(), coordToMove);
        assertEquals(workerToMove.getPreviousPosition(), initialCoord);
    }

    @Test
    public void findAvailablePositionsToMoveTest() {
        HashMap<Coord, ArrayList<Coord>> availablePositions = abstractGodCard.findAvailablePositionsToMove(spyGame);

        ArrayList<Coord> firstWorkerCoords = new ArrayList<>();
        firstWorkerCoords.add(new Coord(1,0));
        firstWorkerCoords.add(new Coord(2,0));
        firstWorkerCoords.add(new Coord(2,1));
        firstWorkerCoords.add(new Coord(2,2));
        firstWorkerCoords.add(new Coord(1,2));
        firstWorkerCoords.add(new Coord(0,2));
        firstWorkerCoords.add(new Coord(0,1));

        ArrayList<Coord> secondWorkerCoords = new ArrayList<>();
        secondWorkerCoords.add(new Coord(3,2));
        secondWorkerCoords.add(new Coord(3, 3));
        secondWorkerCoords.add(new Coord(3,4));
        secondWorkerCoords.add(new Coord(4,4));

        boolean equals = true;
        for (Coord key : availablePositions.keySet()) {
            ArrayList<Coord> actualCoords = availablePositions.get(key);
            if (key.equals(new Coord(1,1))) {
                if (!actualCoords.containsAll(firstWorkerCoords) || actualCoords.size() != firstWorkerCoords.size())
                    equals = false;
            }
            if (key.equals(new Coord(4,3))) {
                if (!actualCoords.containsAll(secondWorkerCoords) || actualCoords.size() != secondWorkerCoords.size())
                    equals = false;
            }
        }

        assertTrue(equals);
    }

    @Test
    public void buildTest() {
        Coord coordWhereToBuild = new Coord(2,4);
        Cell cellBeforeBuild = spyGame.getCellsHandler().getCell(coordWhereToBuild);
        int previousHeight = cellBeforeBuild.getHeight();

        // TODO : are worker and player really necessary in DataToBuild?
        abstractGodCard.build(new DataToBuild(spyGame, null, null, coordWhereToBuild, Boolean.FALSE));

        assertEquals(spyGame.getCellsHandler().getCell(coordWhereToBuild).getHeight(), previousHeight + 1);

        cellBeforeBuild.setHeight(3);
        spyGame.getCellsHandler().changeStateOfCell(cellBeforeBuild, coordWhereToBuild);
        abstractGodCard.build(new DataToBuild(spyGame, null, null, coordWhereToBuild, Boolean.TRUE));

        assertTrue(spyGame.getCellsHandler().getCell(coordWhereToBuild).getOccupiedByDome());
        assertEquals(4, spyGame.getCellsHandler().getCell(coordWhereToBuild).getHeight());
    }

    @Test
    public void findAvailablePositionsToBuildBlockTest() {
        CellsHandler cellsHandler = spyGame.getCellsHandler();

        Coord coordWithDome = new Coord(3, 2);
        Cell cellWithDome = spyGame.getCellsHandler().getCell(coordWithDome);
        cellWithDome.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(cellWithDome, coordWithDome);

        Coord coordWithDome1 = new Coord(4, 4);
        Cell cellWithDome1 = spyGame.getCellsHandler().getCell(coordWithDome1);
        cellWithDome1.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(cellWithDome1, coordWithDome1);

        Coord coordWithDome2 = new Coord(3, 4);
        Cell cellWithDome2 = spyGame.getCellsHandler().getCell(coordWithDome2);
        cellWithDome2.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(cellWithDome1, coordWithDome2);

        Coord coordLevelThree = new Coord(3, 3);
        Cell cellLevelThree = spyGame.getCellsHandler().getCell(coordLevelThree);
        cellLevelThree.setHeight(3);
        cellsHandler.changeStateOfCell(cellLevelThree, coordLevelThree);

        ArrayList<Coord> expected = new ArrayList<>();
        expected.add(new Coord(1,0));
        expected.add(new Coord(2,0));
        expected.add(new Coord(2,1));
        expected.add(new Coord(2,2));
        expected.add(new Coord(1,2));
        expected.add(new Coord(0,2));
        expected.add(new Coord(0,1));

        HashMap<Coord, ArrayList<Coord>> availablePositions = abstractGodCard.findAvailablePositionsToBuildBlock(spyGame);

        boolean equals = true;
        for (Coord key : availablePositions.keySet()) {
            ArrayList<Coord> actualCoords = availablePositions.get(key);
            assertNotEquals(key, new Coord(4, 3));

            if (key.equals(new Coord(1,1))) {
                if (!actualCoords.containsAll(expected) || actualCoords.size() != expected.size())
                    equals = false;
            }
        }

        assertTrue(equals);
    }

    @Test
    public void findAvailablePositionsToBuildDomeTest() {
        CellsHandler cellsHandler = spyGame.getCellsHandler();

        Coord coordWithDome = new Coord(3, 2);
        Cell cellWithDome = spyGame.getCellsHandler().getCell(coordWithDome);
        cellWithDome.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(cellWithDome, coordWithDome);

        Coord coordWithDome1 = new Coord(4, 4);
        Cell cellWithDome1 = spyGame.getCellsHandler().getCell(coordWithDome1);
        cellWithDome1.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(cellWithDome1, coordWithDome1);

        Coord coordWithDome2 = new Coord(3, 4);
        Cell cellWithDome2 = spyGame.getCellsHandler().getCell(coordWithDome2);
        cellWithDome2.setOccupiedByDome(true);
        cellsHandler.changeStateOfCell(cellWithDome1, coordWithDome2);

        Coord coordLevelThree = new Coord(3, 3);
        Cell cellLevelThree = spyGame.getCellsHandler().getCell(coordLevelThree);
        cellLevelThree.setHeight(3);
        cellsHandler.changeStateOfCell(cellLevelThree, coordLevelThree);

        ArrayList<Coord> expected = new ArrayList<>();
        expected.add(coordLevelThree);

        HashMap<Coord, ArrayList<Coord>> availablePositions = abstractGodCard.findAvailablePositionsToBuildDome(spyGame);

        boolean equals = true;
        for (Coord key : availablePositions.keySet()) {
            ArrayList<Coord> actualCoords = availablePositions.get(key);
            assertNotEquals(key, new Coord(1, 1));

            if (key.equals(new Coord(4,3))) {
                if (!actualCoords.containsAll(expected) || actualCoords.size() != expected.size())
                    equals = false;
            }
        }

        assertTrue(equals);
    }
}