package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class CardsHandlerForTesting extends CardsHandler {
    /**
     * Non default constructor that initializes the attributes of the object
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