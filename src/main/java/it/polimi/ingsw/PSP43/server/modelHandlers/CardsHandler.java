package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CardAlreadyInUseException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CardsHandler {
    @XmlElementWrapper
    @XmlElement (name = "card")
    private ArrayList<AbstractGodCard> deckOfAbstractGodCards;
    private HashMap<String, AbstractGodCard> mapOwnerCard;

    public CardsHandler() {
        this.mapOwnerCard = new HashMap<>();
        this.deckOfAbstractGodCards = new ArrayList<>();
        // TODO : CODICE CHE ACCEDE AL FILE XML E INIZIALIZZA TUTTE LE CARTE DEL DECK
    }

    private ArrayList<AbstractGodCard> getDeckOfAbstractGodCards() {
        return deckOfAbstractGodCards;
    }

    private void setDeckOfAbstractGodCards(ArrayList<AbstractGodCard> deckOfAbstractGodCards) {
        this.deckOfAbstractGodCards = deckOfAbstractGodCards;
    }

    private void setMapOwnerCard(HashMap<String, AbstractGodCard> mapOwnerCard) {
        this.mapOwnerCard = mapOwnerCard;
    }

    private HashMap<String, AbstractGodCard> getCardsInUse() {
        return mapOwnerCard;
    }

    /**
     *
     * @param nickOwner The data stored about a player into the list of players
     * @param godName The data of the card chosen by a player during the game
     * @throws CardAlreadyInUseException
     */
    public void setCardToPlayer(String nickOwner, String godName) throws CardAlreadyInUseException {
        for (String key : mapOwnerCard.keySet()) {
            if (godName.equals(mapOwnerCard.get(key))) throw new CardAlreadyInUseException("We are sorry but the " + godName + "has been" +
                    "already chosen. Select another card to continue.");
        }

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
