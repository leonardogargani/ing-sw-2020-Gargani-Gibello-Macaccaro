package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

public class ChosenCardResponse extends ClientMessage {
    private static final long SerialVersionUID = 383838393939373737L;
    private AbstractGodCard card;

    public ChosenCardResponse(AbstractGodCard card){
        this.card = card;
    }

    public AbstractGodCard getCard() {
        return card;
    }
}
