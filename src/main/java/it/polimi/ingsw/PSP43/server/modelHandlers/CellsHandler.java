package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;


public class CellsHandler {
    private static final int DIM = 5;
    private Cell[][] board;

    /**
     * Not default constructor for initialize the game board
     */
    public CellsHandler(){
        board = new Cell[DIM][DIM];
        for(int i = 0;i < DIM;i++)
            for (int j = 0;j < DIM;j++)
                board[i][j]= new Cell();

    }

    /**
     * Method to get the game board useful to other method of that class
     * @return the searched cell
     */
    public Cell getCell(Coord c) {
        return board[c.getX()][c.getY()];

        /*Cell c1;
        c1 = new Cell(board[c.getX()][c.getY()].getHeight(),board[c.getX()][c.getY()].getOccupiedByWorker(),board[c.getX()][c.getY()].getOccupiedByDome());
        return c1;*/
    }



    /**
     * Method tu update cell's attributes
     * @param newDescriptionCell is a cell received from a client that specifies the new state of a cell, identified by c
     * @param c includes the coordinates of the cell that are going to be modified
     */
    public void changeStateOfCell (Cell newDescriptionCell, Coord c) throws CellAlreadyOccupiedExeption, CellHeightException {
        if (newDescriptionCell.getHeight()>3) throw new CellHeightException("You can't set an height that is more than three!!!");
        getCell(c).setHeight(newDescriptionCell.getHeight());

        if(newDescriptionCell.getOccupiedByWorker()&newDescriptionCell.getOccupiedByDome()) throw new CellAlreadyOccupiedExeption("A worker and a dome cannot be in the same cell!!!");
        getCell(c).setOccupiedByWorker(newDescriptionCell.getOccupiedByWorker());
        getCell(c).setOccupiedByDome(newDescriptionCell.getOccupiedByDome());

    }
}
