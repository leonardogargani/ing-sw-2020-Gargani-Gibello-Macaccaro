package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.server.model.Cell;


public class CellMessage extends ServerMessage {

    private static final long serialVersionUID = -9103584969031073533L;
    private final Cell cell;


    /**
     * Not default constructor for CellMessage message.
     * @param cell is a cell that will be updated on the board
     */
    public CellMessage(Cell cell){
        this.cell = cell;
    }


    /**
     * Getter method for the cell contained in the message.
     * @return cell
     */
    public Cell getCell() {
        return cell;
    }

}
