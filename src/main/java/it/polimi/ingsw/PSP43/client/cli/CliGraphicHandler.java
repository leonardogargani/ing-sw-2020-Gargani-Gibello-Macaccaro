package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.GraphicHandler;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Worker;


public class CliGraphicHandler extends GraphicHandler {

    private CliBoard board = new CliBoard();


    /**
     * This method returns the whole board of the cli.
     * @return whole board of the cli
     */
    public CliBoard getBoard() {
        return board;
    }


    /**
     * This method updates the cli changing the color of a cell, based
     * on the color of the worker that has been moved.
     * @param worker worker that has been moved
     */
    @Override
    public void updateBoardChange(Worker worker) {
        CliCell newCell = board.getCell(worker.getCurrentPosition());

        // I need to "free" the previous position only if the worker was already on the board:
        // if it has just been place it doesn't have a previous position
        if (worker.getPreviousPosition() != null) {
            CliCell oldCell = board.getCell(worker.getPreviousPosition());
            oldCell.setColor(Color.ANSI_WHITE);
        }

        newCell.setColor(worker.getColor());
    }


    /**
     * This method updates the cli board changing the symbol of a cell, based
     * on the fact that a worker can build on a cell.
     * @param serverCell cell that has changed (a worker has built on it)
     */
    @Override
    public void updateBoardChange(Cell serverCell) {
        CliCell cell = board.getCell(serverCell.getCoord());
        cell.setSymbol(CliCell.SYMBOLS[serverCell.getHeight()]);
    }


    /**
     * This method renders all the graphic aspects of the cli.
     */
    @Override
    public void render() {
        board.show();
    }


}
