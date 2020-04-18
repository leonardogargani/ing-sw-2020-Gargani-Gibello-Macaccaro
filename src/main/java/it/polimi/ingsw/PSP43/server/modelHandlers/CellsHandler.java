package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;

import java.util.ArrayList;


public class CellsHandler {
    private static final int DIM = 5;
    private Cell[][] board;


    /**
     * Not default constructor for initialize the game board
     */
    public CellsHandler() {
        board = new Cell[DIM][DIM];
        for(int i = 0;i < DIM;i++)
            for (int j = 0;j < DIM;j++) {
                board[i][j] = new Cell();
                board[i][j].setCoord(new Coord(i, j));
            }

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

    public Coord[] findAllCellsFree() {
        ArrayList<Coord> freeCells = new ArrayList();
        for (int i=0; i<board.length; i++) {
            for (int j=0; i<board.length; j++) {
                if (!board[i][j].getOccupiedByWorker() && !board[i][j].getOccupiedByDome())
                    freeCells.add(new Coord(i,j));
            }
        }
        return (Coord[]) freeCells.toArray();
    }

    public AbstractIterator iterator(Coord initialPosition, Coord finalPosition) {
        int xIncrement = initialPosition.getX() - finalPosition.getX();
        int yIncrement = initialPosition.getY() - finalPosition.getY();
        return new GenericDirectionIterator(initialPosition, xIncrement, yIncrement);
    }

    public interface AbstractIterator {
        Cell next();
    }

    private class GenericDirectionIterator implements AbstractIterator {
        Coord initialPosition;
        Coord currentPosition;
        int xIncrement;
        int yIncrement;

        public GenericDirectionIterator(Coord initialPosition, int xIncrement, int yIncrement) {
            this.initialPosition = initialPosition;
            this.currentPosition = initialPosition;
            this.xIncrement = xIncrement;
            this.yIncrement = yIncrement;
        }

        public boolean hasNext() {
            if ((currentPosition.getX() + xIncrement == DIM || currentPosition.getX() + xIncrement == 0) && xIncrement != 0) {
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
