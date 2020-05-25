package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.modelHandlers.CardsHandlerForTesting;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class GameSessionForTest extends GameSession {
    private final CardsHandlerForTesting cardsHandlerForTesting;

    public GameSessionForTest(int idgame) throws ParserConfigurationException, SAXException, IOException {
        super(idgame);
        cardsHandlerForTesting = new CardsHandlerForTesting();
    }

    public CardsHandlerForTesting getCardsHandler() {
        return cardsHandlerForTesting;
    }
}
