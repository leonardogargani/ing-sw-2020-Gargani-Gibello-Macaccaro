package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Cell;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CellTest {

    Cell test;

    @Before
    public void setUp() {
        test = new Cell();
    }

    // Tests of the setter and of the getter are the same
    @Test
    public void testGetHeight() {
        test.setHeight(2);
        assertEquals(2, test.getHeight());
    }

    @Test
    public void testSetHeight() {
        test.setHeight(2);
        assertEquals(2, test.getHeight());
    }

    // Tests of the setter and of the getter are the same
    @Test
    public void testGetOccupiedByWorker() {
        test.setOccupiedByWorker(true);
        assertTrue(test.getOccupiedByWorker());
        test.setOccupiedByWorker(false);
        assertFalse(test.getOccupiedByWorker());
    }

    @Test
    public void testSetOccupiedByWorker() {
        test.setOccupiedByWorker(true);
        assertTrue(test.getOccupiedByWorker());
        test.setOccupiedByWorker(false);
        assertFalse(test.getOccupiedByWorker());
    }

    // Tests of the setter and of the getter are the same
    @Test
    public void testGetOccupiedByDome() {
        test.setOccupiedByDome(true);
        assertTrue(test.getOccupiedByDome());
        test.setOccupiedByDome(false);
        assertFalse(test.getOccupiedByDome());
    }

    @Test
    public void testSetOccupiedByDome() {
        test.setOccupiedByDome(true);
        assertTrue(test.getOccupiedByDome());
        test.setOccupiedByDome(false);
        assertFalse(test.getOccupiedByDome());
    }

}