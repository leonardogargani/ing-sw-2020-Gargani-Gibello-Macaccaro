package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionRequest extends TextMessage {
    private static final long SerialVersionUID = 111123456789012345L;
    private HashMap <Coord,ArrayList<Coord>> cellsAvailable;

    /**
     * Not default constructor for ActionRequest message
     * @param message is the string that will be shown to the recipient
     * @param cellsAvailable represents the cells where workers can go in
     */
    public ActionRequest(String message, HashMap<Coord,ArrayList<Coord>> cellsAvailable){
        super(message);
        this.cellsAvailable = cellsAvailable;
    }

    /**
     * Getter method for the string message
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * Getter method for the HashMap cellsAvailable, that contains the coordinates available for a player's workers
     * @return the HashMap cellsAvailable
     */
    public HashMap<Coord,ArrayList<Coord>> getCellsAvailable() {
        return cellsAvailable;
    }
}
