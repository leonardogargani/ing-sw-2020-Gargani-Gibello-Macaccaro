package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * CellsHandler has the task to handle all the cells of the board of the game. It has to make consistent the state of the cells
 * with the game and it has some methods to return externally (for example to the logic in the cards) a cell or a group
 * of cells.
 */
public class CellsHandler {

    private static final int DIM = 5;
    private final Cell[][] board;
    private GameSession gameSession;


    /**
     * Non default constructor that initializes all the cells in the board with their coordinates and with the BoardObserver.
     * @param gameSession gameSession attribute to be set for the current object
     */
    public CellsHandler(GameSession gameSession) {

        this.gameSession = gameSession;
        board = new Cell[DIM][DIM];

        for(int i = 0; i < DIM; i++)
            for (int j = 0; j < DIM; j++) {
                board[i][j] = new Cell(new Coord(i, j), gameSession.getBoardObserver());
            }

    }


    /**
     * Method that returns the gameSession attribute.
     * @return gameSession attribute
     */
    public GameSession getGameSession() { return gameSession; }


    /**
     * Method that sets the gameSession attribute.
     * @param gameSession gameSession attribute
     */
    public void setGameSession(GameSession gameSession) { this.gameSession = gameSession; }


    /**
     * Method to get a cell from the coordinates supplied.
     * @return the cell for which the caller was looking for
     */
    public Cell getCell(Coord c) {
            return board[c.getX()][c.getY()];
    }


    /**
     * Method to update cell's attributes and make them consistent to the game.
     * @param newDescriptionCell is a cell received from a client that specifies the new state of a cell, identified by the coordinates supplied
     * @param newCoord includes the coordinates of the cell that are going to be modified
     */
    public void changeStateOfCell (Cell newDescriptionCell, Coord newCoord) {
        Cell cellToChange = board[newCoord.getX()][newCoord.getY()];
        cellToChange.setOccupiedByDome(newDescriptionCell.getOccupiedByDome());
        cellToChange.setOccupiedByWorker(newDescriptionCell.getOccupiedByWorker());
        cellToChange.setHeight(newDescriptionCell.getHeight());
        cellToChange.setColor(newDescriptionCell.getColor());
    }


    /**
     * This method finds all the free cells of a board and returns their coordinates.
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


    /**
     * This method selects all the free cells (no worker and no dome) within a bunch of coordinates supplied by the caller.
     * @param positions The coordinates supplied by the caller within which the method has to identify the ones free.
     * @return an ArrayList of free coordinates within all the supplied coordinates as parameter of the method.
     */
    public ArrayList<Coord> selectAllFreeCoords(ArrayList<Coord> positions) {

        ArrayList<Coord> newPositions = new ArrayList<>();

        for (Coord c : positions) {
            if (!board[c.getX()][c.getY()].getOccupiedByWorker() && !board[c.getX()][c.getY()].getOccupiedByDome())
                newPositions.add(c.clone());
        }
        return newPositions;

    }


    /**
     * This method finds all the neighbouring cells for the workers provided from the caller.
     * @return an HashMap in which the key value are the coordinates of the workers supplied by the caller and the values are all the neighbouring
     * cells of that worker
     */
    public HashMap<Coord, ArrayList<Coord>> findWorkersNeighbouringCoords(Player player) {

        Integer[] workerIds = player.getWorkersIdsArray();
        List<Worker> workers = gameSession.getWorkersHandler().getWorkers(workerIds);

        HashMap<Coord, ArrayList<Coord>> availablePositions = new HashMap<>();
        ArrayList<Coord> positions;

        for (Worker w : workers) {
            positions = findNeighbouringCoords(w.getCurrentPosition());
            availablePositions.put(w.getCurrentPosition(), positions);
        }
        return availablePositions;

    }


    /**
     * This method identifies a bunch of coordinates that are neighbours of the one supplied by the caller.
     * @param coord The coordinates of which the method has to identify all the neighbouring coordinates.
     * @return An ArrayList of coordinates representing all the neighbours of the coordinates supplied by the caller.
     */
    public ArrayList<Coord> findNeighbouringCoords(Coord coord) {

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
     * This method gets all the cells that are related to the coordinates supplied by the caller.
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

    public int findNumberOfDomes() {
        int numberDomes = 0;

        for (Cell[] cells : board) {
            for (int j = 0; j < board.length; j++) {
                if (cells[j].getHeight() == 4) numberDomes++;
            }
        }

        return numberDomes;
    }

    public boolean isPerimetral(Coord c) {
        return c.getY() == DIM -1 || c.getY() == 0 || c.getX() == DIM -1 || c.getX() == 0;
    }

    public boolean isOppositeCoordFree(Coord coordForcer, Coord coordForced) {
        int xIncrement = coordForcer.getX() - coordForced.getX();
        int yIncrement = coordForcer.getY() - coordForced.getY();

        if ((coordForcer.getX() + xIncrement < 0 || coordForcer.getX() + xIncrement > 4) ||
                (coordForcer.getY() + yIncrement < 0 || coordForcer.getY() + yIncrement > 4)) {
            return false;
        }
        else {
            Cell cellToForce = board[coordForcer.getX() + xIncrement][coordForcer.getY() + yIncrement];
            if (cellToForce.getOccupiedByDome() || cellToForce.getOccupiedByWorker()) {
                return false;
            }
            else return true;
        }
    }

    public Coord findOppositeCoordFree(Coord coordForcer, Coord coordForced) {
        int xIncrement = coordForcer.getX() - coordForced.getX();
        int yIncrement = coordForcer.getY() - coordForced.getY();

        if ((coordForcer.getX() + xIncrement < 0 || coordForcer.getX() + xIncrement > 4) ||
                (coordForcer.getY() + yIncrement < 0 || coordForcer.getY() + yIncrement > 4)) {
            return null;
        }
        else {
            Cell cellToForce = board[coordForcer.getX() + xIncrement][coordForcer.getY() + yIncrement];
            if (cellToForce.getOccupiedByDome() || cellToForce.getOccupiedByWorker()) {
                return null;
            }
            else return new Coord(coordForcer.getX() + xIncrement, coordForcer.getY() + yIncrement);
        }
    }
}
