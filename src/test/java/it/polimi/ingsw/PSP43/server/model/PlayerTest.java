package it.polimi.ingsw.PSP43.server.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class PlayerTest {
    private Player playerTest;

    @Before
    public void setUp() {
        playerTest = new Player("Leo");
    }

    @Test
    public void testGetNickname() {
        assertEquals("Leo", playerTest.getNickname());
    }

    // getter and setter are tested both at the same time
    @Test
    public void testGetSetWorkersIdsArray() {
        Integer[] expectedWorkersIdsArray = new Integer[]{3, 4};
        playerTest.setWorkersIdsArray(expectedWorkersIdsArray);
        Integer[] actualWorkersIdsArray = playerTest.getWorkersIdsArray();

        assertEquals(actualWorkersIdsArray.length, expectedWorkersIdsArray.length);

        for (int i = 0; i < expectedWorkersIdsArray.length; i++) {
            assertSame(expectedWorkersIdsArray[i], actualWorkersIdsArray[i]);
        }
    }
}