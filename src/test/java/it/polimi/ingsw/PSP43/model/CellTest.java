package it.polimi.ingsw.PSP43.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CellTest {

    Cell test;

    @Before
    public void setUp() {
        test = new Cell();
    }

    // getter and setter tested both at the same time
    @Test
    public void testGetSetHeight() {
        test.setHeight(2);
        assertEquals(2, test.getHeight());
    }

    // getter and setter tested both at the same time
    @Test
    public void testGetSetOccupiedByWorker() {
        test.setOccupiedByWorker(true);
        assertTrue(test.getOccupiedByWorker());
        test.setOccupiedByWorker(false);
        assertFalse(test.getOccupiedByWorker());
    }

    // getter and setter tested both at the same time
    @Test
    public void testGetSetOccupiedByDome() {
        test.setOccupiedByDome(true);
        assertTrue(test.getOccupiedByDome());
        test.setOccupiedByDome(false);
        assertFalse(test.getOccupiedByDome());
    }

}