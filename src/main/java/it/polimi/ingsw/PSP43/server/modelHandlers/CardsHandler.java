package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controllers.DecoratorFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;


public class CardsHandler {

    private List<AbstractGodCard> deckOfAbstractGodCards;
    final HashMap<String, AbstractGodCard> mapOwnersCard;


    /**
     * Non default constructor that initializes the attributes of the object.
     *
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
     *
     * @return unmodifiable deckOfAbstractGodCards attribute
     */
    public List<AbstractGodCard> getDeckOfAbstractGodCards() {
        return Collections.unmodifiableList(new ArrayList<>(deckOfAbstractGodCards));
    }


    /**
     * Method that returns the mapOwnersCard attribute as unmodifiable.
     *
     * @return unmodifiable mapOwnersCard attribute
     */
    public Map<String, AbstractGodCard> getMapOwnersCard() {
        return Collections.unmodifiableMap(new HashMap<>(mapOwnersCard));
    }


    /**
     * Method that assigns a God card to the player who has chosen it.
     *
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

    public AbstractGodCard getPlayerCard(String nick) {
        for (String s : mapOwnersCard.keySet()) {
            if (s.equals(nick))
                return mapOwnersCard.get(s);
        }
        return null;
    }

    /**
     * Method that removes a God card from a player.
     *
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
