package it.polimi.ingsw.model;

import it.polimi.ingsw.model.GameSession;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class GenericDescriptorTest {
    @Test
    public void getGameIdentifier() {
        GameSession actual = new GameSession(9);
        GenericDescriptor test = new GenericDescriptor(actual);

        assertEquals(actual, test.getGameIdentifier());
    }
}