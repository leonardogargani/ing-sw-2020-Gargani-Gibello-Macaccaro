package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Worker;


public abstract class GraphicHandler {


    /**
     * This method updates the cli or the gui changing the color of a cell, based
     * on the color of the worker that has been moved.
     * @param worker worker that has been moved
     */
    public abstract void updateBoardChange(Worker worker);


    /**
     * This method updates the cli board changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     * @param serverCell cell that has changed (a worker has built on it)
     */
    public abstract void updateBoardChange(Cell serverCell);


    /**
     * This method renders all the graphic aspects of the cli or the gui.
     */
    public abstract void render();


}
