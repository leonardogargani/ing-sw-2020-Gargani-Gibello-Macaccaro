package it.polimi.ingsw.PSP43.server.controller.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.controller.cards.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controller.cards.DecoratorFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * This class is used to handle all the cards of the game in a deck and to keep a correspondence
 * of the players' cards.
 */
public class CardsHandler {

    private List<AbstractGodCard> deckOfAbstractGodCards;
    final HashMap<String, AbstractGodCard> mapOwnersCard;


    /**
     * Non default constructor that initializes the attributes of the object.
     * @throws ParserConfigurationException exception thrown if
     * @throws SAXException                 exception thrown if
     * @throws IOException                  exception thrown if
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
        return Collections.unmodifiableList(new ArrayList<>(deckOfAbstractGodCards));
    }


    /**
     * Method that returns the mapOwnersCard attribute as unmodifiable.
     * @return unmodifiable mapOwnersCard attribute
     */
    public Map<String, AbstractGodCard> getMapOwnersCard() {
        return Collections.unmodifiableMap(new HashMap<>(mapOwnersCard));
    }


    /**
     * Method that assigns a God card to the player who has chosen it.
     * @param nickOwner data stored about a player into the list of players
     * @param godName   data of the card chosen by a player during the game
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
     * This method finds the card owned by the player that has the nickname passed as parameter.
     * @param nick The nick of the player used to find the card owned.
     * @return The card owned by the player that has the nickname passed as parameter.
     */
    public AbstractGodCard getPlayerCard(String nick) {
        for (String s : mapOwnersCard.keySet()) {
            if (s.equals(nick))
                return mapOwnersCard.get(s);
        }
        return null;
    }

    /**
     * Method that removes a God card from a player.
     * @param nickOwner data stored about a player into the list of players
     */
    public void removeCardToPlayer(String nickOwner) {
        for (Iterator<Map.Entry<String, AbstractGodCard>> iterator = mapOwnersCard.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, AbstractGodCard> entry = iterator.next();
            String currentNickOwner = entry.getKey();
            if (nickOwner.equals(currentNickOwner)) iterator.remove();
        }
    }


    /**
     * This method adds a decorator to all the cards owned by the players (excluding the card with the name passed as parameter).
     * @param godNameAdding The name of the god that is adding the specified decorator.
     * @param factory The factory of the decorator that is used to add it to the cards of the players.
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
     * This method removes from all the cards owned by players (the one with the name passed as parameter excluded) the decorator
     * passed as parameter.
     * @param godNameRemoving The name of the god that is removing from all the cards the specified decorator.
     * @param decorator The name of the decorator that has to be removed from all the card owned during the game (the one with the name passed as parameter excluded).
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
