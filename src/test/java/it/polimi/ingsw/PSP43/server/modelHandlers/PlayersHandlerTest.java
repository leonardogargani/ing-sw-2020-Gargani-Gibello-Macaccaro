package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CardAlreadyInUseException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import static org.junit.Assert.*;

public class PlayersHandlerTest {
    PlayersHandler handlerTest;

    /**
     * This method initialises a set of players taking their nicknames from a csv file and the cards' data they own
     */
    /*
    @Before
    public void setUp() throws Exception {
        handlerTest = new PlayersHandler();
        File f;
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource("txtFiles/Players.txt");
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            f = new File(resource.getFile());
        }
        BufferedReader in = new BufferedReader(new FileReader(f));
        String line;
        while((line = in.readLine()) != null) {
            String[] parts = line.split(",");
            handlerTest.createNewPlayer(parts[0]);
            Player p = handlerTest.getPlayer(parts[0]);
            AbstractGodCard c = new BasicGodCard(parts[1], parts[2], null);
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
        AbstractGodCard actualAbstractGodCard = new BasicGodCard("Atlas", "Your Worker may build a dome at any level.", null);
        handlerTest.createNewPlayer(actualNickname);
        Player actualPlayer = handlerTest.getPlayer(actualNickname);
        handlerTest.setCardToPlayer(actualPlayer, actualAbstractGodCard);
        boolean equals = false;
        if (actualPlayer.getAbstractGodCard().getGodName().equals(actualAbstractGodCard.getGodName()) && actualPlayer.getAbstractGodCard().getDescription().equals(actualAbstractGodCard.getDescription()))
            equals = true;
        assertTrue(equals);
    }

     */
}