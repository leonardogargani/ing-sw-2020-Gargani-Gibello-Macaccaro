package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * CellsHandler has the task to handle all the cells of the board of the game. It has to make consistent the state of the cells
 * with the game and it has some methods to return externally (for example to the logic in the cards) a cell or a group
 * of cells.
 */
public class CellsHandler {
    private static final int DIM = 5;
    private final Cell[][] board;
    private GameSession gameSession;

    public CellsHandler(GameSession gameSession) {
        this.gameSession = gameSession;
        board = new Cell[DIM][DIM];
        for(int i = 0;i < DIM;i++)
            for (int j = 0;j < DIM;j++) {
                board[i][j] = new Cell(new Coord(i, j), gameSession.getBoardObserver());
            }
    }

    public GameSession getGameSession() { return gameSession; }

    public void setGameSession(GameSession gameSession) { this.gameSession = gameSession; }

    /**
     * Method to get a cell from the coordinates supplied
     * @return the cell for which the caller was looking for
     */
    public Cell getCell(Coord c) {
            return board[c.getX()][c.getY()];
    }

    /**
     * Method to update cell's attributes and make them consistent to the game.
     * @param newDescriptionCell is a cell received from a client that specifies the new state of a cell, identified by the coordinates supplied
     * @param c includes the coordinates of the cell that are going to be modified
     */
    public void changeStateOfCell (Cell newDescriptionCell, Coord c) throws IOException {
        Cell cellToChange = board[c.getX()][c.getY()];
        cellToChange.setOccupiedByDome(newDescriptionCell.getOccupiedByDome());
        cellToChange.setOccupiedByWorker(newDescriptionCell.getOccupiedByWorker());
        cellToChange.setHeight(newDescriptionCell.getHeight());
    }

    /**
     * This method finds all the free cells of a board and returns their coordinates
     * @return an ArrayList containing all the coordinates of the free cells
     */
    public ArrayList<Coord> findAllFreeCoords() {
        ArrayList<Coord> freeCells = new ArrayList<>();
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board.length; j++) {
                if (!board[i][j].getOccupiedByWorker() && !board[i][j].getOccupiedByDome())
                    freeCells.add(new Coord(i,j));
            }
        }
        return freeCells;
    }

    public ArrayList<Coord> selectAllFreeCoords(ArrayList<Coord> positions) {
        ArrayList<Coord> newPositions = new ArrayList<>();
        for (Coord c : positions) {
            if (!board[c.getX()][c.getY()].getOccupiedByWorker() && !board[c.getX()][c.getY()].getOccupiedByDome())
                newPositions.add(c.clone());
        }
        return newPositions;
    }

    /**
     * This method finds all the neighbouring cells for the workers provided from the caller
     * @param workers references of the workers of which the method has to find the neighbouring cells
     * @return an HashMap in which the key value are the coordinates of the workers supplied by the caller and the values are all the neighbouring
     * cells of that worker
     */
    public HashMap<Coord, ArrayList<Coord>> findWorkersNeighbouringCoords(ArrayList<Worker> workers) {
        HashMap<Coord, ArrayList<Coord>> availablePositions = new HashMap<>();
        ArrayList<Coord> positions;

        for (Worker w : workers) {
            positions = findNeighbouringCoords(w.getCurrentPosition());
            availablePositions.put(w.getCurrentPosition(), positions);
        }
        return availablePositions;
    }

    /**
     * This method finds all the neighbouring cells for the worker provided from the caller
     * @param worker reference of the worker of which the method has to find the neighbouring cells
     * @return an HashMap in which the key value are the coordinates of the worker supplied by the caller and the values are all the neighbouring
     * cells of that worker
     */
    public HashMap<Coord, ArrayList<Coord>> findWorkerNeighbouringCoords(Worker worker) {
        HashMap<Coord, ArrayList<Coord>> availablePositions = new HashMap<>();
        ArrayList<Coord> positions;

        positions = findNeighbouringCoords(worker.getCurrentPosition());
        availablePositions.put(worker.getCurrentPosition(), positions);
        return availablePositions;
    }

    private ArrayList<Coord> findNeighbouringCoords(Coord coord) {
        ArrayList<Coord> positions = new ArrayList<>();

        for (int i=-1; i<2 ; i++) {
            for (int j=-1; j<2; j++) {
                if (coord.getX() + i > -1 && coord.getX() + i < DIM) {
                    if (coord.getY() + j > -1 && coord.getY() + j < DIM) {
                        if (i!=0 || j!=0) {
                            positions.add(new Coord(coord.getX() + i, coord.getY() + j));
                        }
                    }
                }
            }
        }
        return positions;
    }

    /**
     * This method finds all the coordinates of the board that are included in the same direction defined from the supplied coordinates.
     * (e.g init = 0,1, final = 0,2, returnValue = (0,3),(0,4))
     * @param initialPosition the initial position from where I have to define the direction to identify the other cells
     * @param finalPosition the final position that defines the direction into which I have to look for the cells
     * @return all the cells (apart the initial and final) that are included into the same direction defined looking from initial to final position and
     * following them
     */
    public ArrayList<Coord> findSameDirectionCoords(Coord initialPosition, Coord finalPosition) {
        int xIncrement = finalPosition.getX() - initialPosition.getX();
        int yIncrement = finalPosition.getY() - initialPosition.getY();
        ArrayList<Coord> coords = new ArrayList<>();

        Coord currentPosition = new Coord(finalPosition.getX(), finalPosition.getY());

        while (!((currentPosition.getX() + xIncrement >= DIM || currentPosition.getX() + xIncrement < 0) && xIncrement != 0) && !((currentPosition.getY() + yIncrement >= DIM || currentPosition.getY() + yIncrement < 0) && yIncrement != 0)) {
            currentPosition = new Coord(currentPosition.getX() + xIncrement, currentPosition.getY() + yIncrement);

            coords.add(currentPosition);
        }

        return coords;
    }

    /**
     * This method gets all the cells that are related to the coordinates supplied by the caller
     * @param coordsToMatch the coordinates for which the method has to find all related cells
     * @return an ArrayList containing all the related cells to the coordinates contained into the ArrayList supplied
     */
    public ArrayList<Cell> getCells(ArrayList<Coord> coordsToMatch) {
        ArrayList<Cell> clonedCellsRequired = new ArrayList<>();

        for (Coord c : coordsToMatch) {
            clonedCellsRequired.add((board[c.getX()][c.getY()]).clone());
        }

        return clonedCellsRequired;
    }
}
