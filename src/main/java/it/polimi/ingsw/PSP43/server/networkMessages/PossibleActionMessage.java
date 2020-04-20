package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

import java.util.ArrayList;

public class PossibleActionMessage extends TextMessage {
    private static final long SerialVersionUID = 111123456789012345L;
    private ArrayList<Coord> cellsAvailable;


    public PossibleActionMessage(String message,ArrayList<Coord> cellsAvailable){
        super(message);
        this.cellsAvailable = cellsAvailable;
    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public ArrayList<Coord> getCellsAvailable() {
        return cellsAvailable;
    }
}
