package it.polimi.ingsw.PSP43.server.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GenericDescriptorTest {
    @Test
    public void getGameIdentifier() {
        GameSession actual = new GameSession(9);
        GenericDescriptor test = new GenericDescriptor(actual);

        assertEquals(actual, test.getGameIdentifier());
    }
}