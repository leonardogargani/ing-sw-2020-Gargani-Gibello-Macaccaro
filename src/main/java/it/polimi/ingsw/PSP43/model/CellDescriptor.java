package it.polimi.ingsw.PSP43.model;

/**
 * Cell description arriving from the net and traslated into a message for a specified GameSession
 */
public class CellDescriptor extends GenericDescriptor {
    private Cell cellReference;
    private int topLevel;
    private boolean worker;
    private boolean dome;

    /**
     *
     * @param gameIdentifier Identification of the object GameSession to which the message is to be sent and handled
     * @param cellRef Reference to the data of cell to modify
     * @param topLevel Height of a cell (0 is base floor, 1 is first floor (1 block) ecc...)
     * @param worker Flag that specifies the presence of a worker into the cell
     * @param dome Flag that specifies the presence of a dome into the cell
     */
    public CellDescriptor(GameSession gameIdentifier, Cell cellRef, int topLevel, boolean worker, boolean dome) {
        super(gameIdentifier);
        this.cellReference = cellRef;
        this.topLevel = topLevel;
        this.worker = worker;
        this.dome = dome;
    }

    /**
     * @return The reference to the cell to modify
     */
    public Cell getCellReference() {
        return cellReference;
    }


    /**
     *
     * @return The top level of a cell, that represent how many blocks are placed (0 if zero blocks, 1 if one block...)
     */
    public int getTopLevel() {
        return topLevel;
    }

    /**
     *
     * @return The flag that indicates the presence of a worker
     */
    public boolean getWorker() {
        return worker;
    }

    /**
     *
     * @return The flag that indicates the presence of a dome
     */
    public boolean getDome() {
        return dome;
    }
}
