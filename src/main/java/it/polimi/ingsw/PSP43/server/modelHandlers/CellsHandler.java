package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;

import java.util.Iterator;


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
        Cell cellToBeCopied = board[c.getX()][c.getY()];
        Cell cellClone = new Cell();
        cellClone.setHeight(cellToBeCopied.getHeight());
        cellClone.setOccupiedByDome(cellToBeCopied.getOccupiedByDome());
        cellClone.setOccupiedByWorker(cellToBeCopied.getOccupiedByWorker());
        return cellToBeCopied;
    }



    /**
     * Method tu update cell's attributes
     * @param newDescriptionCell is a cell received from a client that specifies the new state of a cell, identified by c
     * @param c includes the coordinates of the cell that are going to be modified
     */
    public void changeStateOfCell (Cell newDescriptionCell, Coord c) throws CellAlreadyOccupiedExeption, CellHeightException {
        Cell cellToChange = board[c.getX()][c.getY()];
        cellToChange.setOccupiedByDome(newDescriptionCell.getOccupiedByDome());
        cellToChange.setOccupiedByWorker(newDescriptionCell.getOccupiedByWorker());
        cellToChange.setHeight(newDescriptionCell.getHeight());
    }

    public AbstractIterator iterator(Coord initialPosition, Coord finalPosition) {
        int xIncrement = initialPosition.getX() - finalPosition.getX();
        int yIncrement = initialPosition.getY() - finalPosition.getY();
        if (xIncrement != 0 && yIncrement != 0)
            return new DiagonalIterator(initialPosition, xIncrement, yIncrement);
        else return new LinearIterator(initialPosition, xIncrement, yIncrement);
    }

    public interface AbstractIterator {
        public Cell next();
    }

    private class DiagonalIterator implements AbstractIterator {
        Coord initialPosition;
        Coord currentPosition;
        int xIncrement;
        int yIncrement;

        public DiagonalIterator(Coord initialPosition, int xIncrement, int yIncrement) {
            this.initialPosition = initialPosition;
            this.currentPosition = initialPosition;
            this.xIncrement = xIncrement;
            this.yIncrement = yIncrement;
        }

        public boolean hasNext() {
            if (((currentPosition.getX() + xIncrement) == DIM || (currentPosition.getX() + xIncrement) == 0)) {
                return false;
            }
            else if (((currentPosition.getY() + yIncrement) == DIM || (currentPosition.getY() + yIncrement) == 0)) {
                return false;
            }
            else return true;
        }

        public Cell next() {
            if (this.hasNext()) {
                currentPosition = new Coord(currentPosition.getX() + xIncrement, currentPosition.getY() + yIncrement);
                return getCell(currentPosition);
            }
            else return null;
        }
    }

    private class LinearIterator implements AbstractIterator {
        Coord initialPosition;
        Coord currentPosition;
        int xIncrement;
        int yIncrement;

        public LinearIterator(Coord initialPosition, int xIncrement, int yIncrement) {
            this.initialPosition = initialPosition;
            this.currentPosition = initialPosition;
            this.xIncrement = xIncrement;
            this.yIncrement = yIncrement;
        }

        public boolean hasNext() {
            if ((currentPosition.getX() + xIncrement == DIM || currentPosition.getX() + xIncrement == DIM) && xIncrement != 0) {
                return false;
            }
            else if ((currentPosition.getY() + yIncrement == DIM || currentPosition.getY() + yIncrement == 0) && yIncrement != 0) {
                return false;
            }
            else return true;
        }

        public Cell next() {
            if (this.hasNext()) {
                currentPosition = new Coord(currentPosition.getX() + xIncrement, currentPosition.getY() + yIncrement);
                return getCell(currentPosition);
            }
            else return null;
        }
    }
}
