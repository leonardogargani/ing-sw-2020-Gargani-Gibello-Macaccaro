package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Coord;

import java.util.ArrayList;

public class PossibleActionMessage extends ResMessage {
    private static final long SerialVersionUID = 111123456789012345L;
    private ArrayList<Coord> cellsAvailableToMove;
    private ArrayList<Coord> cellsAvailableToBuild;

    public PossibleActionMessage(int idGame, ArrayList<Coord> cellsAvailableToMove, ArrayList<Coord> cellsAvailableToBuild){
        super(idGame);
        this.cellsAvailableToMove = cellsAvailableToMove;
        this.cellsAvailableToBuild = cellsAvailableToBuild;
    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public ArrayList<Coord> getCellsAvailableToMove() {
        return cellsAvailableToMove;
    }

    public ArrayList<Coord> getCellsAvailableToBuild() {
        return cellsAvailableToBuild;
    }
}
