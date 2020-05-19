package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.DecoratorFactory;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;


public class CardsHandler {

    private List<AbstractGodCard> deckOfAbstractGodCards;
    private final HashMap<String, AbstractGodCard> mapOwnersCard;


    /**
     * Non default constructor that initializes the attributes of the object.
     * @throws ParserConfigurationException exception thrown if
     * @throws SAXException exception thrown if
     * @throws IOException exception thrown if
     */
    public CardsHandler() throws ParserConfigurationException, SAXException, IOException {
        this.mapOwnersCard = new HashMap<>();
        this.deckOfAbstractGodCards = new ArrayList<>();
        deckOfAbstractGodCards = DOMCardsParser.buildDeck();
    }


    /**
     * Method that returns the deckOfAbstractGodCards attribute as unmodifiable.
     * @return unmodifiable deckOfAbstractGodCards attribute
     */
    public List<AbstractGodCard> getDeckOfAbstractGodCards() {
        return Collections.unmodifiableList(deckOfAbstractGodCards);
    }


    /**
     * Method that returns the mapOwnersCard attribute as unmodifiable.
     * @return unmodifiable mapOwnersCard attribute
     */
    public Map<String, AbstractGodCard> getMapOwnersCard() {
        return Collections.unmodifiableMap(mapOwnersCard);
    }


    /**
     * Method that assigns a God card to the player who has chosen it.
     * @param nickOwner data stored about a player into the list of players
     * @param godName data of the card chosen by a player during the game
     */
    public void setCardToPlayer(String nickOwner, String godName) {
        AbstractGodCard abstractGodCardToSet = null;
        for (AbstractGodCard c : deckOfAbstractGodCards) {
            if (c.getGodName().equals(godName)) {
                abstractGodCardToSet = c;
            }
        }
        mapOwnersCard.put(nickOwner, abstractGodCardToSet);
    }


    /**
     * Method that removes a God card from a player.
     * @param nickOwner data stored about a player into the list of players
     */
    public void removeCardToPlayer(String nickOwner) {
        for (String key : mapOwnersCard.keySet()) {
            if (nickOwner.equals(key)) mapOwnersCard.remove(nickOwner);
        }
    }


    /**
     * Method that prints all the cards in a nicely formatted way.
     */
    public void print() {
        int i = 0;
        for (AbstractGodCard c : deckOfAbstractGodCards) {
            i++;
            System.out.println("CARD " + i + " : ");
            c.print();
        }
    }


    /**
     *
     * @param godNameAdding
     * @param factory
     */
    public void addDecorator(String godNameAdding, DecoratorFactory factory) {
        AbstractGodCard cardOwned;

        for (String s : getMapOwnersCard().keySet()) {
            cardOwned = getMapOwnersCard().get(s);
            if (!cardOwned.getGodName().equals(godNameAdding)) {
                mapOwnersCard.put(s, factory.buildDecorator(cardOwned));
            }
        }
    }


    /**
     *
     * @param godNameRemoving
     * @param decorator
     */
    public void removeDecorator(String godNameRemoving, String decorator) {

        AbstractGodCard card, newCard;

        for (String s : mapOwnersCard.keySet()) {
            String currentGodName = mapOwnersCard.get(s).getGodName();
            if (!currentGodName.equals(godNameRemoving)) {
                card = mapOwnersCard.get(s);
                newCard = card.cleanFromEffects(decorator);
                mapOwnersCard.put(s, newCard);
            }
        }
    }

}
