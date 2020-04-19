package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Worker;


public interface BoardHandler {


    /**
     * This method updates the cli board changing the color of a cell, based
     * on the color of the worker that has been moved.
     * @param worker worker that has been moved
     */
    void updateBoardChange(Worker worker);


    /**
     * This method updates the cli board changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     * @param serverCell cell that has changed (a worker has built on it)
     */
    void updateBoardChange(Cell serverCell);


}
