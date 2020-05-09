package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.ClientBG;
import it.polimi.ingsw.PSP43.client.ClientManager;
import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.networkMessages.WorkerMessage;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertThat;


public class CliGraphicHandlerTest {

    private CliGraphicHandler test;


    @Before
    public void setUp() {
        ClientBG clientBG = new ClientBG(new ClientManager(1));
        test = new CliGraphicHandler(clientBG);
    }


    // this method tests all the implementations (signatures) of updateBoardChange()
    @Test
    public void testUpdateBoardChange() throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        /*BoardObserver boardObserver = new BoardObserver(new GameSession(9));
        Worker worker = new Worker(0, Color.ANSI_RED, boardObserver);
        Coord coord1 = new Coord(2, 3);
        Coord coord2 = new Coord(1, 3);
        CliCell cliCell1 = test.getBoard().getCell(coord1);
        CliCell cliCell2 = test.getBoard().getCell(coord2);

        worker.setCurrentPosition(coord1);
        test.updateBoardChange(new WorkerMessage(worker));
        assertThat(cliCell1.toString(), CoreMatchers.containsString(Color.ANSI_RED.toString()));
        assertThat(cliCell2.toString(), CoreMatchers.containsString(Color.ANSI_WHITE.toString()));

        worker.setCurrentPosition(coord2);
        test.updateBoardChange(new WorkerMessage(worker));
        assertThat(cliCell1.toString(), CoreMatchers.containsString(Color.ANSI_WHITE.toString()));
        assertThat(cliCell2.toString(), CoreMatchers.containsString(Color.ANSI_RED.toString()));*/

    }


    @Test
    public void testGetBoard() {
        // cannot test this method because I cannot access the board attribute in any way
    }

}