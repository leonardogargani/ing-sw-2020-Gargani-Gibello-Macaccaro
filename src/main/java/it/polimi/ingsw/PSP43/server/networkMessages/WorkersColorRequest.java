package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.Color;

import java.util.ArrayList;

public class WorkersColorRequest extends TextMessage {
    private static final long SerialVersionUID=112345678901234567L;
    private ArrayList<Color> colorsAvailable;
    public WorkersColorRequest(String message, ArrayList<Color> colorsAvailable){
        super(message);
        this.colorsAvailable = colorsAvailable;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ArrayList<Color> getColorsAvailable() {
        return colorsAvailable;
    }
}
