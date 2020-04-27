package it.polimi.ingsw.PSP43.client.gui;

import it.polimi.ingsw.PSP43.client.GraphicHandler;
import it.polimi.ingsw.PSP43.server.networkMessages.CellMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.WorkerMessage;


public class GuiGraphicHandler extends GraphicHandler {


    /**
     * This method updates the cli or the gui changing the color of a cell, based
     * on the color of the worker that has been moved.
     *
     * @param workerMessage message containing the worker that has been moved
     */
    @Override
    public void updateBoardChange(WorkerMessage workerMessage) {

    }

    /**
     * This method updates the cli board changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     *
     * @param cellMessage message containing the cell that has changed (a worker has built on it)
     */
    @Override
    public void updateBoardChange(CellMessage cellMessage) {

    }

    /**
     * This method renders all the graphic aspects of the cli or the gui.
     */
    @Override
    public void render() {

    }
}
