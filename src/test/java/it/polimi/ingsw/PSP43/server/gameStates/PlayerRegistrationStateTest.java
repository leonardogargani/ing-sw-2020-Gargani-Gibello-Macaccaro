package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.BoardObserver;
import it.polimi.ingsw.PSP43.server.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.GameInitialiser;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.NicknameAlreadyInUseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerRegistrationStateTest {
    GameSession gameSession;
    GameSession spyGame;
    BoardObserver obs;
    BoardObserver spyObs;
    ArrayList<AbstractGodCard> deck;
    ChooseCardState state;
    ChooseCardState spyState;

    @Before
    public void setUp() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, NicknameAlreadyInUseException {
        gameSession = GameInitialiser.initialiseGame();
        spyGame = Mockito.spy(gameSession);
        obs = new BoardObserver();
        spyObs = Mockito.spy(obs);
        deck = DOMCardsParser.buildDeck();
        state = new ChooseCardState(spyGame);
        spyState = Mockito.spy(state);
    }

    @Test
    public void executeState() {
    }

    @Test
    public void findNextState() {
    }
}