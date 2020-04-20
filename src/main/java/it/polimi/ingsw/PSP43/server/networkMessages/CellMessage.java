package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Cell;

public class CellMessage extends ServerMessage {
    private static final long SerialVersionUID=887766554433221100L;
    private Cell cell;

    public CellMessage(Cell cell){
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }
}
