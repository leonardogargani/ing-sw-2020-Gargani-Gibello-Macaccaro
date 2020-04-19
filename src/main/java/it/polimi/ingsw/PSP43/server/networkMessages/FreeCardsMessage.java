package it.polimi.ingsw.PSP43.server.networkMessages;

import javax.smartcardio.Card;
import java.util.ArrayList;

public class FreeCardsMessage extends ResMessage {
    private static final long SerialVersionUID=123456789012345678L;
    private ArrayList<Card> cards;

    //rivedo parte carte
    public FreeCardsMessage(int idGame, ArrayList<Card> cards){
        super(idGame);
        this.cards=cards;
    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
