package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Cell;

public class CellMessage extends ServerMessage {
    private static final long SerialVersionUID=887766554433221100L;
    private Cell cell;

    /**
     * Not default constructor for CellMessage message
     * @param cell is a cell that will be updated on the board
     */
    public CellMessage(Cell cell){
        this.cell = cell;
    }

    /**
     * Getter method for the cell contained in the message
     * @return cell
     */
    public Cell getCell() {
        return cell;
    }
}
