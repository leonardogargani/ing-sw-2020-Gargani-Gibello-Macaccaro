package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.Color;

public class WorkersColorResponse extends ClientMessage {
    private static final long SerialVersionUID=432109876543210987L;
    private Color color;

    public WorkersColorResponse(Color color){
        this.color = color;
    }

    public WorkersColorResponse() {
    }

    public Color getColor() {
        return color;
    }
}
