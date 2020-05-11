package it.polimi.ingsw.PSP43.client.cli;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;


public class CliTopMenuTest {

    private CliTopMenu test;
    private ByteArrayOutputStream outContent;


    @Before
    public void setUp() {
        test = new CliTopMenu();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    // methods show() and setContent() are tested both at the same time
    @Test
    public void testShowSetContent() {
        test.setContent("This is a test");
        test.show();
        assertEquals(("This is a test" + System.lineSeparator()), outContent.toString());
    }

    // methods show() and setContentWihNick() are tested both at the same time
    @Test
    public void testShowSetContentWithNick() {
        test.setContentWithNick("TestNick");
        test.show();
        assertEquals(("          -------------------------------------          " + "\n" +
                              "                    It's TestNick's turn" + "\n" +
                              "          -------------------------------------          " + "\n" + System.lineSeparator()),
                outContent.toString());
    }
}