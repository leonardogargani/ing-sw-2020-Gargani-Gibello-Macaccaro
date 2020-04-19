package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Worker;

import java.util.ArrayList;
import java.util.HashMap;


public class CellsHandler {
    private static final int DIM = 5;
    private Cell[][] board;
    private GameSession gameSession;


    /**
     * Not default constructor for initialize the game board
     */
    public CellsHandler(GameSession gameSession) {
        this.gameSession = gameSession;
        board = new Cell[DIM][DIM];
        for(int i = 0;i < DIM;i++)
            for (int j = 0;j < DIM;j++) {
                board[i][j] = new Cell(new Coord(i, j), gameSession.getGraphicObserver());
            }

    }

    /**
     * Method to get the game board useful to other method of that class
     * @return the searched cell
     */
    public Cell getCell(Coord c) {
            return board[c.getX()][c.getY()];
    }



    /**
     * Method tu update cell's attributes
     * @param newDescriptionCell is a cell received from a client that specifies the new state of a cell, identified by c
     * @param c includes the coordinates of the cell that are going to be modified
     */
    public void changeStateOfCell (Cell newDescriptionCell, Coord c) {
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

    public HashMap<Coord, ArrayList<Integer>> findNeighbouringCells(Worker[] workers) {
        HashMap<Coord, ArrayList<Integer>> availablePositions = new HashMap<>();

        for (Worker w : workers) {
            Coord currentCoord = w.getCurrentPosition();
            for (int i=-1; i<2 ; i++) {
                for (int j=-1; j<2; j++) {
                    if (currentCoord.getX() + i > -1 && currentCoord.getX() + i < DIM) {
                        if (currentCoord.getY() + j > -1 && currentCoord.getY() + j < DIM) {
                            if (i!=0 && j!=0) {
                                ArrayList<Integer> positions = availablePositions.get(currentCoord);
                                if (positions == null) {
                                    positions = new ArrayList<>();
                                    positions.add(w.getId());
                                    availablePositions.put(board[i][j].getCoord(), positions);
                                }
                                else {
                                    positions.add(w.getId());
                                    availablePositions.put(board[i][j].getCoord(), positions);
                                }
                            }
                        }
                    }
                }
            }
        }
        return availablePositions;
    }

    public AbstractIterator directionIterator(Coord initialPosition, Coord finalPosition) {
        int xIncrement = initialPosition.getX() - finalPosition.getX();
        int yIncrement = initialPosition.getY() - finalPosition.getY();
        return new GenericDirectionIterator(initialPosition, xIncrement, yIncrement);
    }


    public interface AbstractIterator {
        Cell next();
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
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
