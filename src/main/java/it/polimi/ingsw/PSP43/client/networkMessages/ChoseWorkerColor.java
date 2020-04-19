package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.Color;

public class ChoseWorkerColor extends ReqMessage {
    private static final long SerialVersionUID=432109876543210987L;
    private Color color;

    public ChoseWorkerColor(String nick, Color color){
        super(nick);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String getNick() {
        return super.getNick();
    }
}
