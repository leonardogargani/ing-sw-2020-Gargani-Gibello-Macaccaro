package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.Color;
import java.util.List;


public class WorkersColorRequest extends ServerMessage {

    private static final long serialVersionUID = -1959452039599220074L;
    private final List<Color> colorsAvailable;

    /**
     * Not default constructor for WorkersColorRequest message.
     * @param colorsAvailable is a List of available colors
     */
    public WorkersColorRequest(List<Color> colorsAvailable){
        this.colorsAvailable = colorsAvailable;
    }


    /**
     * Getter method for the List colorsAvailable.
     * @return colorsAvailable
     */
    public List<Color> getColorsAvailable() {
        return colorsAvailable;
    }

}
