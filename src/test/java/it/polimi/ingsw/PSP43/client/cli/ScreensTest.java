package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.Color;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ScreensTest {

    private Screens test;

    @Test
    public void testToString() {
        test = Screens.LOSER;
        assertEquals(Color.ANSI_RED +
                "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                "*                                                       *\n" +
                "*          Y o u   l o s t   t h e   g a m e !          *\n" +
                "*                                                       *\n" +
                "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                Color.RESET, test.toString());
        test = Screens.WELCOME;
        assertEquals(Color.ANSI_GREEN +
                "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                "*                                                       *\n" +
                "*                       W e l c o m e                   *\n" +
                "*                            T o                        *\n" +
                "*                     S A N T O R I N I                 *\n" +
                "*                                                       *\n" +
                "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                Color.RESET, test.toString());
        test = Screens.WINNER;
        assertEquals(Color.ANSI_GREEN +
                "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                "*                                                       *\n" +
                "*           Y o u   w o n   t h e   g a m e !           *\n" +
                "*                                                       *\n" +
                "* * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
                Color.RESET, test.toString());
        test = Screens.YOUR_TURN;
        assertEquals(Color.ANSI_WHITE +
                "          -------------------------------------          \n" +
                "          |          It's your turn           |          \n" +
                "          -------------------------------------          \n" +
                Color.RESET, test.toString());
    }

}