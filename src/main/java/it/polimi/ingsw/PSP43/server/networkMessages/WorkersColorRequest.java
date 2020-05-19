package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.Color;
import java.util.List;


public class WorkersColorRequest extends TextMessage {

    private static final long serialVersionUID = -1959452039599220074L;
    private final List<Color> colorsAvailable;


    /**
     * Not default constructor for WorkersColorRequest message.
     * @param message is the string that will be shown to the recipient
     * @param colorsAvailable is an ArrayList of available colors
     */
    public WorkersColorRequest(String message, List<Color> colorsAvailable){
        super(message);
        this.colorsAvailable = colorsAvailable;
    }


    /**
     * Getter method for the string message.
     * @return message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }


    /**
     * Getter method for the ArrayList colorsAvailable.
     * @return colorsAvailable
     */
    public List<Color> getColorsAvailable() {
        return colorsAvailable;
    }

}
