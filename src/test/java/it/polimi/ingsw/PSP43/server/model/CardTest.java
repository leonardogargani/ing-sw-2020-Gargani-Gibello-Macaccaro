package it.polimi.ingsw.PSP43.server.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CardTest {

    Card test;

    @Before
    public void setUp() {
        test = new Card("Zeus", "This is the description of Zeus.");
    }

    @Test
    public void testGetGodName() {
        assertEquals("Zeus", test.getGodName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("This is the description of Zeus.", test.getDescription());
    }

}