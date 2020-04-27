package it.polimi.ingsw.PSP43.client.cli;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.client.GraphicHandler;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.networkMessages.CellMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.WorkerMessage;


public class CliGraphicHandler extends GraphicHandler {

    private CliBoard board = new CliBoard();
    private CliTopMenu topMenu = new CliTopMenu();
    private CliMiddleMenu middleMenu = new CliMiddleMenu();
    private CliBottomMenu bottomMenu = new CliBottomMenu();

    /**
     * This method returns the whole board of the cli.
     * @return whole board of the cli
     */
    public CliBoard getBoard() {
        return board;
    }


    /**
     * This method updates the cli or the gui changing the color of a cell, based
     * on the color of the worker that has been moved.
     * @param workerMessage message containing the worker that has been moved
     */
    @Override
    public void updateBoardChange(WorkerMessage workerMessage) {
        Worker worker = workerMessage.getWorker();
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
     * @param cellMessage message containing the cell that has changed (a worker has built on it)
     */
    @Override
    public void updateBoardChange(CellMessage cellMessage) {
        Cell serverCell = cellMessage.getCell();
        CliCell cell = board.getCell(serverCell.getCoord());
        cell.setSymbol(CliCell.SYMBOLS[serverCell.getHeight()]);
    }


    /**
     * This method updates the cli menu displaying a message that notifies players the game has ended.
     * @param message message containing the sentence to display
     */
    @Override
    public void updateMenuChange(EndGameMessage message) {

    }


    /**
     * This method renders all the graphic aspects of the cli.
     */
    @Override
    public void render() {
        // the cli is made of these four graphical components, printed to the screen in the right order
        topMenu.show();
        middleMenu.show();
        board.show();
        bottomMenu.show();
    }


}
