package it.polimi.ingsw.PSP43.server.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellDescriptorTest {
    private GameSession actualGame;
    private Cell actualCellRef;
    private int actualLevel;
    private boolean actualWorker;
    private boolean actualDome;
    private CellDescriptor test;

    @Before
    public void setUp(){
        actualGame = new GameSession(9);
        actualCellRef = new Cell();
        actualLevel = 2;
        actualWorker = false;
        actualDome = true;
        test = new CellDescriptor(actualGame, actualCellRef, actualLevel, actualWorker, actualDome);
    }

    @Test
    public void getGameIdentifier() {
        assertEquals(actualGame, test.getGameIdentifier());
    }

    @Test
    public void getCellReference() {
        assertEquals(actualCellRef, test.getCellReference());
    }

    @Test
    public void getTopLevel() {
        assertTrue(actualLevel == test.getTopLevel());
    }

    @Test
    public void getBlock() {
        assertTrue(actualDome == test.getDome());
    }

    @Test
    public void getWorker() {
        assertTrue(actualWorker == test.getWorker());
    }
}