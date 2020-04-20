package it.polimi.ingsw.PSP43.server.networkMessages;

import javax.smartcardio.Card;
import java.util.ArrayList;

public class CardsMessage extends TextMessage {
    private static final long SerialVersionUID=123456789012345678L;
    private ArrayList<Card> cards;

    //rivedo parte carte
    public CardsMessage(String message,ArrayList<Card> cards){
        super(message);
        this.cards=cards;
    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
