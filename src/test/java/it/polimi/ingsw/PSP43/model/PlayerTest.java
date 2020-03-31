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

    // Tests of the setter and of the getter are the same
    @Test
    public void testGetWorkersArray() {
        Worker[] workersArray = new Worker[2];
        workersArray[0] = new Worker("blue");
        workersArray[1] = new Worker("blue");
        test.setWorkersArray(workersArray);
        assertArrayEquals(workersArray, test.getWorkersArray());
    }

    @Test
    public void testSetWorkersArray() {
        Worker[] workersArray = new Worker[2];
        workersArray[0] = new Worker("blue");
        workersArray[1] = new Worker("blue");
        test.setWorkersArray(workersArray);
        assertArrayEquals(workersArray, test.getWorkersArray());
    }

    // Tests of the setter and of the getter are the same
    @Test
    public void testGetCard() {
        Card card = new Card("Zeus", "This is the description of Zeus.");
        test.setCard(card);
        assertEquals(card, test.getCard());
    }

    @Test
    public void testSetCard() {
        Card card = new Card("Zeus", "This is the description of Zeus.");
        test.setCard(card);
        assertEquals(card, test.getCard());
    }

}