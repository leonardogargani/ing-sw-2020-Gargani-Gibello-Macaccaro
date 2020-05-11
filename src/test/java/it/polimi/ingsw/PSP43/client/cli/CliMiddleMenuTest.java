package it.polimi.ingsw.PSP43.client.cli;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class CliMiddleMenuTest {

    private CliMiddleMenu test;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        test = new CliMiddleMenu();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    // methods show() and setContentWithInfo() are tested both at the same time
    @Test
    public void testShowContentWithInfo() {
        ArrayList<String> infos = new ArrayList<>();
        String info1 = "Player1: godName1 (powerDescription1)";
        String info2 = "Player2: godName2 (powerDescription2)";
        String info3 = "Player3: godName3 (powerDescription3)";
        infos.add(info1);
        infos.add(info2);
        infos.add(info3);
        test.setContentWithInfo(infos);
        test.show();
        assertEquals(info1 + "\n" + info2 + "\n"
                + info3 + "\n" + System.lineSeparator(), outContent.toString());
    }

}