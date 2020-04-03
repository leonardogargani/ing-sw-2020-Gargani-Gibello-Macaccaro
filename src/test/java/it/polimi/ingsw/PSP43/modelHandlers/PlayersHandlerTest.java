package it.polimi.ingsw.PSP43.modelHandlers;

import it.polimi.ingsw.PSP43.model.Card;
import it.polimi.ingsw.PSP43.model.Player;
import it.polimi.ingsw.PSP43.modelHandlersException.CardAlreadyInUseException;
import it.polimi.ingsw.PSP43.modelHandlersException.NicknameAlreadyInUseException;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class PlayersHandlerTest {
    PlayersHandler handlerTest;

    /**
     * This method initialises a set of players taking their nicknames from a csv file and the cards' data they own
     */
    @Before
    public void setUp() throws Exception {
        handlerTest = new PlayersHandler();
        File f = new File(".\\initFiles\\Players.txt");
        BufferedReader in = new BufferedReader(new FileReader(f));
        String line;
        while((line = in.readLine()) != null) {
            String[] parts = line.split(",");
            handlerTest.createNewPlayer(parts[0]);
            Player p = handlerTest.getPlayer(parts[0]);
            Card c = new Card(parts[1], parts[2]);
            handlerTest.setCardToPlayer(p, c);
        }
    }

    @Test
    public void createNewPlayer() throws NicknameAlreadyInUseException {
        String actualNickname = "Nicolò";
        boolean found;
        assertTrue(handlerTest.getPlayer(actualNickname) == null);

        handlerTest.createNewPlayer(actualNickname);

        Player actualPlayer = handlerTest.getPlayer(actualNickname);
        assertTrue(actualPlayer != null && actualPlayer.getNickname().equals(actualNickname));
    }

    @Test
    public void getPlayer() throws NicknameAlreadyInUseException {
        String actualNickname = "Fabio";
        handlerTest.createNewPlayer(actualNickname);

        Player actualPlayer = handlerTest.getPlayer(actualNickname);
        assertTrue(actualPlayer != null && actualPlayer.getNickname().equals(actualNickname));
    }

    @Test
    public void deletePlayer() {
        String actualNickname = "Amilcare";
        Player actualPlayer = handlerTest.getPlayer(actualNickname);
        assertTrue(actualPlayer != null && actualPlayer.getNickname().equals(actualNickname));
        handlerTest.deletePlayer(actualNickname);
        actualPlayer = handlerTest.getPlayer(actualNickname);
        assertTrue(actualPlayer == null);
    }

    @Test
    public void setCardToPlayer() throws NicknameAlreadyInUseException, CardAlreadyInUseException {
        String actualNickname = "Gibi";
        Card actualCard = new Card("Atlas", "Your Worker may build a dome at any level.");
        handlerTest.createNewPlayer(actualNickname);
        Player actualPlayer = handlerTest.getPlayer(actualNickname);
        handlerTest.setCardToPlayer(actualPlayer, actualCard);
        boolean equals = false;
        if (actualPlayer.getCard().getGodName().equals(actualCard.getGodName()) && actualPlayer.getCard().getDescription().equals(actualCard.getDescription()))
            equals = true;
        assertTrue(equals);
    }
}