package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.GameSession;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;


public class WorkersHandlerTest {

    WorkersHandler test;
    GameSession gameSession;

    @Before
    public void setUp() {
        gameSession = new GameSession(2);
        test = new WorkersHandler(gameSession);
    }

    // this test is the same as the one for addNewWorker()
    @Test
    public void testAddNewWorker() {
        Worker worker1 = new Worker(0, Color.ANSI_BLUE);
        Worker worker2 = new Worker(1, Color.ANSI_RED);
        ArrayList<Worker> correctArrayList = new ArrayList<>(Arrays.asList(worker1, worker2));
        test.addNewWorker(Color.ANSI_BLUE);
        test.addNewWorker(Color.ANSI_RED);
        // TODO find a way to compare elements of two ArrayList
        assertEquals(correctArrayList.size(), test.getWorkers().size());
    }

    @Test
    public void testChangePosition() throws CellAlreadyOccupiedExeption {
        Worker worker = new Worker(1, Color.ANSI_YELLOW);
        Coord coord1 = new Coord(1, 2);
        Coord coord2 = new Coord(2, 2);
        worker.setCurrentPosition(coord1);
        test.changePosition(worker, coord2);

        Cell oldCell = gameSession.getCellsHandler().getCell(coord1);
        Cell newCell = gameSession.getCellsHandler().getCell(coord2);
        assertFalse(oldCell.getOccupiedByDome());
        assertFalse(oldCell.getOccupiedByWorker());
        assertFalse(newCell.getOccupiedByDome());
        assertTrue(newCell.getOccupiedByWorker());
    }

    // this test is the same as the one for addNewWorker()
    @Test
    public void testGetWorkers()  {
        Worker worker1 = new Worker(0, Color.ANSI_BLUE);
        Worker worker2 = new Worker(1, Color.ANSI_RED);
        ArrayList<Worker> correctArrayList = new ArrayList<>(Arrays.asList(worker1, worker2));
        test.addNewWorker(Color.ANSI_BLUE);
        test.addNewWorker(Color.ANSI_RED);
        // TODO find a way to compare elements of two ArrayList
        assertEquals(correctArrayList.size(), test.getWorkers().size());
    }

}