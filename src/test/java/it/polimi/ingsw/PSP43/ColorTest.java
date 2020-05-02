package it.polimi.ingsw.PSP43;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import static org.junit.Assert.*;


public class ColorTest {

    private Color test;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testToString() {
        test = Color.ANSI_RED;
        assertEquals("\u001B[31m", test.toString());
    }

    @Test
    public void testPrintName() throws IOException {
        Color.printName(Color.ANSI_BLUE);
        assertEquals("BLUE\n", outContent.toString());
        outContent.reset();
        Color.printName(Color.ANSI_RED);
        assertEquals("RED\n", outContent.toString());
        outContent.reset();
        Color.printName(Color.ANSI_GREEN);
        assertEquals("GREEN\n", outContent.toString());
        outContent.reset();
        Color.printName(Color.ANSI_WHITE);
        assertEquals("This color doesn't exist\n", outContent.toString());
    }

}