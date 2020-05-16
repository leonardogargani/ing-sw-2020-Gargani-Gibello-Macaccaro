package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.*;


public class CellTest {
    private Cell test;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        test = new Cell(new Coord(3, 3), new BoardObserver(new GameSession(9)));
    }

    // getter and setter are tested both at the same time
    @Test
    public void getSetHeightTest() throws IOException {
        test.setHeight(2);
        assertEquals(2, test.getHeight());
    }

    // getter and setter are tested both at the same time
    @Test
    public void testGetSetOccupiedByWorker() throws IOException {
        test.setOccupiedByWorker(true);
        assertTrue(test.getOccupiedByWorker());
        test.setOccupiedByWorker(false);
        assertFalse(test.getOccupiedByWorker());
    }

    // getter and setter are tested both at the same time
    @Test
    public void testGetSetOccupiedByDome() throws IOException {
        test.setOccupiedByDome(false);
        assertFalse(test.getOccupiedByDome());
        assertEquals(0, test.getHeight());

        test.setOccupiedByDome(true);
        assertTrue(test.getOccupiedByDome());
        assertEquals(4, test.getHeight());
    }

    // getter and setter are tested both at the same time
    @Test
    public void getSetCoordTest() {
        Coord coord = new Coord(1, 2);
        test.setCoord(coord);
        assertEquals(coord, test.getCoord());
    }

    @Test
    public void getSetColorTest() throws IOException {
        test.setColor(Color.ANSI_BLUE);

        assertSame(test.getColor(), Color.ANSI_BLUE);
    }

    @Test
    public void testClone() {
        Cell cellCloned = test.clone();

        assertSame(cellCloned.getColor(), test.getColor());
        assertEquals(cellCloned.getOccupiedByDome(), test.getOccupiedByDome());
        assertEquals(cellCloned.getOccupiedByWorker(), test.getOccupiedByWorker());
        assertEquals(cellCloned.getHeight(), test.getHeight());
        assertEquals(cellCloned.getCoord(), test.getCoord());
    }
}