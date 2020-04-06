package it.polimi.ingsw.PSP43.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class PlayerTest {

    Player test;

    @Before
    public void setUp() {
        test = new Player("Leo");
    }

    @Test
    public void testGetNickname() {
        assertEquals("Leo", test.getNickname());
    }

    // getter and setter are tested both at the same time
    @Test
    public void testGetSetWorkersIdsArray() {
        int[] workersIdsArray = new int[]{3, 4};
        test.setWorkersIdsArray(3, 4);
        assertArrayEquals(workersIdsArray, test.getWorkersIdsArray());
    }

    @Test
    public void testGetSetCard() {
        Card card = new Card("Zeus", "This is the description of Zeus.");
        test.setCard(card);
        assertEquals(card, test.getCard());
    }

}