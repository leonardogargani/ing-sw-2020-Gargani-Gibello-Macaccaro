package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.gameStates.GameSessionObservable;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.*;


public class WorkerTest {
    private Worker test;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        BoardObserver boardObserver = new BoardObserver(new GameSession(9));
        test = new Worker(5, Color.ANSI_BLUE);
    }

    // getter and setter are tested both at the same time
    @Test
    public void testGetSetCurrentPosition() throws IOException {
        Coord coord = new Coord(1, 2);
        test.setCurrentPosition(coord);
        assertEquals(coord, test.getCurrentPosition());
    }

    @Test
    public void testGetPreviousPosition() throws IOException {
        Coord firstCoord = new Coord(1, 2);
        Coord secondCoord = new Coord(2, 2);
        test.setCurrentPosition(firstCoord);
        test.setCurrentPosition(secondCoord);
        assertEquals(firstCoord, test.getPreviousPosition());
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