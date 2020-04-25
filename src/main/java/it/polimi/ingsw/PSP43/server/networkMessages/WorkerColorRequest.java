package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.Color;

import java.util.ArrayList;

public class WorkerColorRequest extends TextMessage {
    private static final long SerialVersionUID=112345678901234567L;
    private ArrayList<Color> colors;
    public WorkerColorRequest(String message, ArrayList<Color> colors){
        super(message);
        this.colors = colors;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ArrayList<Color> getColors() {
        return colors;
    }
}
