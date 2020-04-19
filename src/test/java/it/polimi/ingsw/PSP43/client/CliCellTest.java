package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.Color;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CliCellTest {

    CliCell test;

    @Before
    public void setUp() {
        test = new CliCell();
    }

    // since I don't have the getter method for the color attribute, I can test the setter
    // method checking the presence of the color string is contained in the string
    // representation of the whole cell
    @Test
    public void testSetColor() {
        test.setColor(Color.ANSI_RED);
        assertThat(test.toString(), CoreMatchers.containsString(Color.ANSI_RED.toString()));
        assertThat(test.toString(), CoreMatchers.containsString(Color.RESET));
    }

    @Test
    public void testSetSymbol() {
        test.setSymbol(CliCell.SYMBOLS[0]);
        assertThat(test.toString(), CoreMatchers.containsString(CliCell.SYMBOLS[0]));
        assertThat(test.toString(), CoreMatchers.containsString(Color.RESET));
    }

    @Test
    public void testToString() {
        test.setColor(Color.ANSI_RED);
        test.setSymbol(CliCell.SYMBOLS[0]);
        assertEquals(test.toString(), Color.ANSI_RED.toString() + CliCell.SYMBOLS[0] + Color.RESET);
    }

}