package it.polimi.ingsw.PSP43.client.gui;

import it.polimi.ingsw.PSP43.client.GraphicHandler;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Worker;


public class GuiGraphicHandler extends GraphicHandler {


    /**
     * This method updates the gui changing the color of a cell, based
     * on the color of the worker that has been moved.
     * @param worker worker that has been moved
     */
    @Override
    public void updateBoardChange(Worker worker) {
        // TODO implement this method after JavaFX explanation
    }


    /**
     * This method updates the gui changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     * @param serverCell cell that has changed (a worker has built on it)
     */
    @Override
    public void updateBoardChange(Cell serverCell) {
        // TODO implement this method after JavaFX explanation
    }


    /**
     * This method renders all the graphic aspects of the gui.
     */
    @Override
    public void render() {
        // TODO implement this method after JavaFX explanation
    }


}
