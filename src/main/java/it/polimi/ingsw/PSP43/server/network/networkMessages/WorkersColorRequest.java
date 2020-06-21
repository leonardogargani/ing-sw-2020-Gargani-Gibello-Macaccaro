package it.polimi.ingsw.PSP43.server.network.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Color;
import java.util.List;


public class WorkersColorRequest extends ServerMessage {

    private static final long serialVersionUID = -1959452039599220074L;
    private final List<Color> colorsAvailable;

    public enum WorkersColorRequestHeader {
        CHOICE,
        SKIP_CHOICE;
    }

    private final WorkersColorRequestHeader workersColorRequestHeader;

    /**
     * Not default constructor for WorkersColorRequest message.
     * @param colorsAvailable is a List of available colors
     */
    public WorkersColorRequest(List<Color> colorsAvailable, WorkersColorRequestHeader workersColorRequestHeader){
        this.colorsAvailable = colorsAvailable;
        this.workersColorRequestHeader = workersColorRequestHeader;
    }


    /**
     * Getter method for the List colorsAvailable.
     * @return colorsAvailable
     */
    public List<Color> getColorsAvailable() {
        return colorsAvailable;
    }

    public WorkersColorRequestHeader getWorkersColorRequestHeader() {
        return workersColorRequestHeader;
    }
}
