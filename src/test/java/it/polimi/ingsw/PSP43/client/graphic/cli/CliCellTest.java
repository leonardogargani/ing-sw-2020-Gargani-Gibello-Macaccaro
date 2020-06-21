package it.polimi.ingsw.PSP43.client.graphic.cli;

import it.polimi.ingsw.PSP43.client.graphic.cli.CliCell;
import it.polimi.ingsw.PSP43.server.model.Color;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CliCellTest {

    private CliCell test;

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
        test.markAsFree(true);
        assertEquals(test.toString(),
                Color.ANSI_YELLOW_BACKGROUND + Color.ANSI_RED.toString() + CliCell.SYMBOLS[0] + Color.RESET);
    }


}