package it.polimi.ingsw.PSP43.client.networkMessages;

import it.polimi.ingsw.PSP43.Color;

public class WorkerColorResponse extends ClientMessage {
    private static final long SerialVersionUID=432109876543210987L;
    private Color color;

    public WorkerColorResponse(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
