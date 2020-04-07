package it.polimi.ingsw.PSP43.server.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorkerDescriptorTest {

    private GameSession actualGame;
    private Worker actualWorker;
    private Cell actualPosition;
    private String actualColor;
    private WorkerDescriptor test;

    @Before
    public void setUp() {
        actualGame = new GameSession(9);
        actualWorker = new Worker(5, "blue");
        actualPosition = new Cell();
        actualColor = "blue";
        test = new WorkerDescriptor(actualGame, actualWorker, actualPosition, actualColor);
    }

    @Test
    public void getGameIdentifier() {
        assertEquals(actualGame, test.getGameIdentifier());
    }

    @Test
    public void getWorker() {
        assertEquals(actualWorker, test.getWorker());
    }

    @Test
    public void getNextPosition() {
        assertEquals(actualPosition, test.getNextPosition());
    }

    @Test
    public void getColor() {
        assertEquals(actualColor, test.getColor());
    }
}