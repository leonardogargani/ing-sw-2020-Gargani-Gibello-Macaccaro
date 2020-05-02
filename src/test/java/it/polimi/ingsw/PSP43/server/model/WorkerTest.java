package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class WorkerTest {

    private Worker test;

    @Before
    public void setUp() {
        BoardObserver boardObserver = new BoardObserver();
        test = new Worker(5, Color.ANSI_BLUE, boardObserver);
    }

    // getter and setter are tested both at the same time
    @Test
    public void testGetSetCurrentPosition() {
        Coord coord1 = new Coord(1, 2);
        test.setCurrentPosition(coord1);
        assertEquals(coord1, test.getCurrentPosition());
    }

    @Test
    public void testGetPreviousPosition() {
        Coord coord1 = new Coord(1, 2);
        Coord coord2 = new Coord(2, 2);
        test.setCurrentPosition(coord1);
        test.setCurrentPosition(coord2);
        assertEquals(coord1, test.getPreviousPosition());
    }

    @Test
    public void testGetColor() {
        assertEquals(Color.ANSI_BLUE, test.getColor());
    }

    @Test
    public void testGetId() {
        assertEquals(5, test.getId());
    }

}