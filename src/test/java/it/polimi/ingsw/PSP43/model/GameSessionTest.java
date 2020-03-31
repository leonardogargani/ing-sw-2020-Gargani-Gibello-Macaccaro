package it.polimi.ingsw.PSP43.model;

import java.util.ArrayList;
import static org.junit.Assert.*;

import it.polimi.ingsw.PSP43.ClientListener;
import org.junit.Before;
import org.junit.Test;

public class GameSessionTest {
    private GameSession actualGame;
    int id=6;
    // private TurnState turnState;
    private ArrayList<ClientListener> players;
    private ClientListener a;
    private Player p1;
    @Before
    public void setUp(){
        actualGame = new GameSession(id);
        a = new ClientListener();
        boolean add1= actualGame.addGamer(a);
        p1= new Player("Roberto");
    }

    /*@Test
    public void changeModelDescriptor() {

    }*/

    @Test
    public void getGamers(){
        ClientListener b= new ClientListener();
        actualGame.addGamer(b);
        assertEquals(2,actualGame.getGamers().size());
    }

    @Test
    public void addGamer(){
        ClientListener c = new ClientListener();
        boolean add2= actualGame.addGamer(c);
        assertEquals(2,actualGame.getGamers().size());
        /*
        oppure
        assertTrue(actualGame.addGamer(a))
         */
    }



    @Test
    public void removeGamer(){
        actualGame.removeGamer(a);
        assertEquals(0,actualGame.getGamers().size());
        /*
        oppure
        assertTrue(actualGame.removeGamers
         */
    }

    @Test
    public void getIdGame() {
        assertEquals(actualGame.getIdGame(),id);
    }

    @Test
    public void getCurrentState() {}

    @Test
    public void setCurrentState() {}

    @Test
    public void getCurrentPlayer() {
        actualGame.setCurrentPlayer(p1);
        assertEquals(p1,actualGame.getCurrentPlayer());
    }

    @Test
    public void setCurrentPlayer() {
        actualGame.setCurrentPlayer(p1);
        assertEquals(p1,actualGame.getCurrentPlayer());
    }

    @Test
    public void getCellHandler() {
        assertFalse(actualGame.getCellsHandler()==null);
    }

    @Test
    public void getPlayerHandler() {
        assertFalse(actualGame.getPlayerHandler()==null);
    }

    @Test
    public void getWorkersHandler() {
        assertFalse(actualGame.getWorkersHandler()==null);
    }

    @Test
    public void getCardsHandler() {
        assertFalse(actualGame.getCardsHandler()==null);
    }
}