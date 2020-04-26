package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.DOMCardsParser;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CardAlreadyInUseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CardsHandler {
    private ArrayList<AbstractGodCard> deckOfAbstractGodCards;
    private HashMap<String, AbstractGodCard> mapOwnerCard;

    public CardsHandler() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        this.mapOwnerCard = new HashMap<>();
        this.deckOfAbstractGodCards = new ArrayList<>();
        DOMCardsParser parser = new DOMCardsParser();
        deckOfAbstractGodCards = DOMCardsParser.buildDeck();
    }

    public ArrayList<AbstractGodCard> getDeckOfAbstractGodCards() {
        return deckOfAbstractGodCards;
    }

    // TODO : REMEMBER TO CHANGE INTO PRIVATE!!!!
    public void setDeckOfAbstractGodCards(ArrayList<AbstractGodCard> deckOfAbstractGodCards) {
        this.deckOfAbstractGodCards = deckOfAbstractGodCards;
    }

    private void setMapOwnerCard(HashMap<String, AbstractGodCard> mapOwnerCard) {
        this.mapOwnerCard = mapOwnerCard;
    }

    public HashMap<String, AbstractGodCard> getMapOwnerCard() {
        return mapOwnerCard;
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
        mapOwnerCard.put(nickOwner, abstractGodCardToSet);
    }

    public void removeCardToPlayer(String nickOwner) {
        for (String key : mapOwnerCard.keySet()) {
            if (nickOwner.equals(key)) mapOwnerCard.remove(nickOwner);
        }
    }

    public AbstractGodCard getCardOwned(String name) {
        return mapOwnerCard.get(name);
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
