package it.polimi.ingsw.PSP43;

import org.junit.Test;
import static org.junit.Assert.*;


public class ColorTest {

    Color test;


    @Test
    public void testToString() {
        test = Color.ANSI_RED;
        assertEquals(test.toString(), "\u001B[31m");
    }

}