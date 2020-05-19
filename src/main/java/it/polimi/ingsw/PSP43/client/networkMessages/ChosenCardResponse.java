package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;


public class ChosenCardResponse extends ClientMessage {

    private static final long SerialVersionUID = 383838393939373737L;
    private AbstractGodCard card;


    /**
     * Not default constructor for ChosenCardResponse message.
     * @param card is the chosen AbstractGodCard
     */
    public ChosenCardResponse(AbstractGodCard card){
        this.card = card;
    }


    /**
     * ChosenCardResponse constructor without parameters.
     */
    public ChosenCardResponse() {
    }


    /**
     * Getter method for the AbstractGodCard card.
     * @return card
     */
    public AbstractGodCard getCard() {
        return card;
    }

}
