package it.polimi.ingsw.PSP43.server.model;

import java.util.ArrayList;
import static org.junit.Assert.*;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.modelHandlersException.FullGameSessionException;
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
    public void setUp() throws FullGameSessionException {
        actualGame = new GameSession(id);
        a = new ClientListener();
        RegistrationMessage message = new RegistrationMessage(9, "Rick", false);
        actualGame.addGamer(a, message);
        p1= new Player("Roberto");
    }

    /*@Test
    public void changeModelDescriptor() {

    }*/

    @Test
    public void getGamers() throws FullGameSessionException {
        ClientListener b= new ClientListener();
        RegistrationMessage message = new RegistrationMessage(9, "Rick", false);
        actualGame.addGamer(b, message);
        assertEquals(2,actualGame.getListenersHashMap().size());
    }

    @Test
    public void addGamer() throws FullGameSessionException {
        ClientListener c = new ClientListener();
        RegistrationMessage message = new RegistrationMessage(9, "Mark", false);
        actualGame.addGamer(c, message);
        assertEquals(2,actualGame.getListenersHashMap().size());
        /*
        oppure
        assertTrue(actualGame.addGamer(a))
         */
    }

    @Test
    public void removeGamer(){
        RegistrationMessage message = new RegistrationMessage(9, "Rick", true);
        actualGame.removeGamer(message);
        assertEquals(0,actualGame.getListenersHashMap().size());
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
        assertFalse(actualGame.getPlayersHandler()==null);
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