package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CliBoardHandlerTest {

    CliBoardHandler test;


    @Before
    public void setUp() {
        test = new CliBoardHandler();
    }


    // this method tests all the implementations (signatures) of updateBoardChange()
    @Test
    public void testUpdateBoardChange() {
        Worker worker = new Worker(0, Color.ANSI_RED);
        Coord coord1 = new Coord(2, 3);
        Coord coord2 = new Coord(1, 3);
        CliCell cliCell1 = test.getBoard().getCell(coord1);
        CliCell cliCell2 = test.getBoard().getCell(coord2);

        worker.setCurrentPosition(coord1);
        test.updateBoardChange(worker);
        assertThat(cliCell1.toString(), CoreMatchers.containsString(Color.ANSI_RED.toString()));
        assertThat(cliCell2.toString(), CoreMatchers.containsString(Color.ANSI_WHITE.toString()));

        worker.setCurrentPosition(coord2);
        test.updateBoardChange(worker);
        assertThat(cliCell1.toString(), CoreMatchers.containsString(Color.ANSI_WHITE.toString()));
        assertThat(cliCell2.toString(), CoreMatchers.containsString(Color.ANSI_RED.toString()));

    }


    @Test
    public void testGetBoard() {
        // cannot test this method because I cannot access the board attribute in any way
    }

}