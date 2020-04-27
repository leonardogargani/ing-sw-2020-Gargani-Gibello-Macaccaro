package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionRequest extends TextMessage {
    private static final long SerialVersionUID = 111123456789012345L;
    private HashMap <Coord,ArrayList<Coord>> cellsAvailable;


    public ActionRequest(String message, HashMap<Coord,ArrayList<Coord>> cellsAvailable){
        super(message);
        this.cellsAvailable = cellsAvailable;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public HashMap<Coord,ArrayList<Coord>> getCellsAvailable() {
        return cellsAvailable;
    }
}
