package it.polimi.ingsw.PSP43.server.controller.modelHandlers;

import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class CardsHandlerForTesting extends CardsHandler {
    /**
     * Non default constructor that initializes the attributes of the object
     * @throws ParserConfigurationException when an error on parsing occurs.
     * @throws SAXException when an error on parsing occurs.
     * @throws IOException when an I/O exception of some sort has occurred.
     */
    public CardsHandlerForTesting() throws ParserConfigurationException, SAXException, IOException { super(); }

    /**
     * Method that assigns a God card to the player who has chosen it.
     *
     * @param nickOwner data stored about a player into the list of players
     * @param card   data of the card chosen by a player during the game
     */
    public void setCardToPlayer(String nickOwner, AbstractGodCard card) {
        super.mapOwnersCard.put(nickOwner, card);
    }
}
