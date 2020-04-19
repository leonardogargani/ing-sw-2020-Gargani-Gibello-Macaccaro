package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.server.model.Coord;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class CliBoardTest {

    CliBoard test;


    @Before
    public void setUp() {
        test = new CliBoard();
    }


    @Test
    public void testShow() {
        // cannot test this method because it only contains print() statements
    }


    // since the setter method for the cell attribute doesn't exist, I can check the
    // related getter method comparing the string representations of two empty cells
    @Test
    public void testGetCell() {
        Coord coord = new Coord(1, 2);
        CliCell cell = new CliCell();
        assertEquals(cell.toString(), test.getCell(coord).toString());
    }

}