package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.initialisers.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CardAlreadyInUseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CardsHandler {
    private ArrayList<AbstractGodCard> deckOfAbstractGodCards;
    private HashMap<String, AbstractGodCard> mapOwnersCard;

    public CardsHandler() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        this.mapOwnersCard = new HashMap<>();
        this.deckOfAbstractGodCards = new ArrayList<>();
        deckOfAbstractGodCards = DOMCardsParser.buildDeck();
    }

    public ArrayList<AbstractGodCard> getDeckOfAbstractGodCards() {
        return deckOfAbstractGodCards;
    }

    private void setDeckOfAbstractGodCards(ArrayList<AbstractGodCard> deckOfAbstractGodCards) {
        this.deckOfAbstractGodCards = deckOfAbstractGodCards;
    }

    private void setMapOwnersCard(HashMap<String, AbstractGodCard> mapOwnersCard) {
        this.mapOwnersCard = mapOwnersCard;
    }

    public HashMap<String, AbstractGodCard> getMapOwnersCard() {
        return mapOwnersCard;
    }

    /**
     *
     * @param nickOwner The data stored about a player into the list of players
     * @param godName The data of the card chosen by a player during the game
     * @throws CardAlreadyInUseException
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

    public void removeCardToPlayer(String nickOwner) {
        for (String key : mapOwnersCard.keySet()) {
            if (nickOwner.equals(key)) mapOwnersCard.remove(nickOwner);
        }
    }

    public AbstractGodCard getCardOwned(String name) {
        return mapOwnersCard.get(name);
    }

    public void print() {
        int i = 0;
        for (AbstractGodCard c : deckOfAbstractGodCards) {
            i++;
            System.out.println("CARD " + i + " : ");
            c.print();
        }
    }
}
