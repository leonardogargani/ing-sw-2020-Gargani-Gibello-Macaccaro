package it.polimi.ingsw.PSP43.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class WorkerTest {

    Worker test;

    @Before
    public void setUp() {
        test = new Worker(5, "blue");
    }

    // getter and setter tested both at the same time
    @Test
    public void testGetSetCurrentPosition() {
        Cell cell1 = new Cell();
        test.setCurrentPosition(cell1);
        assertEquals(cell1, test.getCurrentPosition());
    }

    @Test
    public void testGetPreviousPosition() {
        Cell cell1 = new Cell();
        Cell cell2 = new Cell();
        test.setCurrentPosition(cell1);
        test.setCurrentPosition(cell2);
        assertEquals(cell1, test.getPreviousPosition());
    }

    @Test
    public void testGetColor() {
        assertEquals("blue", test.getColor());
    }

    @Test
    public void testGetId() {
        assertEquals(5, test.getId());
    }

}