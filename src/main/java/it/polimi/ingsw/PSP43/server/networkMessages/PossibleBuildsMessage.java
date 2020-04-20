package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

import java.util.ArrayList;

public class PossibleBuildsMessage extends PossibleActionMessage {
    private static final long SerialVersionUID=556556556556556556L;

    public PossibleBuildsMessage(String message, ArrayList<Coord> cellsAvailable){super(message,cellsAvailable);}
}
