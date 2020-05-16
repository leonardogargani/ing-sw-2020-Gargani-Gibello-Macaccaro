package it.polimi.ingsw.PSP43.server.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordTest {
    private Coord coord;
    private final int cX = 3;
    private final int cY = 4;

    @Before
    public void setUp() {
        coord = new Coord(cX, cY);
    }

    @Test
    public void getX() {
        assertEquals(cX, coord.getX());
    }

    @Test
    public void getY() {
        assertEquals(cY, coord.getY());
    }

    @Test
    public void testEquals() {
        Coord clone = coord.clone();

        assertEquals(clone, coord);
        assertNotEquals(6, coord);
    }

    @Test
    public void testClone() {
        Coord clone = coord.clone();

        assertTrue(clone.getX() == cX && clone.getY() == cY);
    }
}