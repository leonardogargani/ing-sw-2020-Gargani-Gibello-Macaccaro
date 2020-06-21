package it.polimi.ingsw.PSP43.client.graphic.cli;

import it.polimi.ingsw.PSP43.client.graphic.cli.CliBottomMenu;
import  org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;


public class CliBottomMenuTest {

    private CliBottomMenu test;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        test = new CliBottomMenu();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    // methods show() and setContent() are tested both at the same time
    @Test
    public void testShowSetContent() {
        String s = "This is a test";
        test.setContent(s);
        test.show();
        assertEquals((s + System.lineSeparator()), outContent.toString());
    }
}