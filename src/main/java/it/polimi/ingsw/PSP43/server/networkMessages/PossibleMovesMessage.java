package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

import java.util.ArrayList;

public class PossibleMovesMessage extends PossibleActionMessage {
    private static final long SerialVersionUID=665665665665665665L;

    public PossibleMovesMessage(String message, ArrayList<Coord> cellsAvailable){super(message,cellsAvailable);}
}
