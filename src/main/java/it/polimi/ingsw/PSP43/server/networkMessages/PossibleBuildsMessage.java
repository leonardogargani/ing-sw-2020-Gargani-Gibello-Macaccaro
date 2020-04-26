package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

import java.util.ArrayList;
import java.util.HashMap;

public class PossibleBuildsMessage extends PossibleActionMessage {
    private static final long SerialVersionUID=556556556556556556L;

    public PossibleBuildsMessage(String message, HashMap<Coord,ArrayList<Coord>> cellsAvailable){super(message,cellsAvailable);}

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public HashMap<Coord, ArrayList<Coord>> getCellsAvailable() {
        return super.getCellsAvailable();
    }
}
