package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;

public class ChoseCardMessage extends ClientMessage {
    private static final long SerialVersionUID=321098765432109876L;
    private AbstractGodCard card;

    public ChoseCardMessage(AbstractGodCard card){
        this.card = card;
    }

    public AbstractGodCard getCardName() {
        return card;
    }
}
