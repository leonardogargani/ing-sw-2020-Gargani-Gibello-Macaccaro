package it.polimi.ingsw.PSP43.modelHandlers;

import it.polimi.ingsw.PSP43.model.GameSession;
import it.polimi.ingsw.PSP43.model.Worker;
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
        Worker worker1 = new Worker(0, "blue");
        Worker worker2 = new Worker(1, "red");
        ArrayList<Worker> correctArrayList = new ArrayList<>(Arrays.asList(worker1, worker2));
        test.addNewWorker("blue");
        test.addNewWorker("red");
        // TODO find a way to compare elements of two ArrayList
        assertEquals(correctArrayList.size(), test.getWorkers().size());
    }

    @Test
    public void testChangePosition() {
        // TODO implement this whole test
    }

    // this test is the same as the one for addNewWorker()
    @Test
    public void testGetWorkers()  {
        Worker worker1 = new Worker(0, "blue");
        Worker worker2 = new Worker(1, "red");
        ArrayList<Worker> correctArrayList = new ArrayList<>(Arrays.asList(worker1, worker2));
        test.addNewWorker("blue");
        test.addNewWorker("red");
        // TODO find a way to compare elements of two ArrayList
        assertEquals(correctArrayList.size(), test.getWorkers().size());
    }

}