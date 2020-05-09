package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.BoardObserver;

import java.io.IOException;

/**
 * Cell is the class that represents a single board square. It can be occupied either by a worker or a dome.
 * Workers can move into a cell or build a tower on it if unoccupied.
 * A cell is characterized by the height its tower has reached, up to three blocks and a dome at the top.
 */
public class Cell extends Observable {

    private static final long serialVersionUID = -8379686866554865826L;

    private int height;
    private boolean occupiedByWorker;
    private boolean occupiedByDome;
    private Coord coord;
    private Color color;


    public Cell(Coord coord, BoardObserver observer) {
        super(observer);
        this.coord = coord;
        this.color = Color.ANSI_WHITE;
    }

    /**
     * This method returns the height (number of blocks) of the tower built on the cell.
     * @return the height (number of blocks) of the tower built on the cell
     */
    public int getHeight(){
        return height;
    }


    /**
     * This method sets the height (number of blocks) of the tower built on the cell.
     * @param height the height (number of blocks) of the tower built on the cell
     */
    public void setHeight(int height) throws IOException {
        this.height = height;
        notifyBoardChange();
    }


    /**
     * This method returns true if the cell is occupied by a worker, false otherwise
     * @return boolean representing if the cell is occupied by a worker or not
     */
    public boolean getOccupiedByWorker() {
        return occupiedByWorker;
    }


    /**
     * This method sets if the cell is occupied by a worker or not.
     * @param occupiedByWorker boolean representing if the cell is occupied by a worker or not
     */
    public void setOccupiedByWorker(boolean occupiedByWorker) throws IOException {
        this.occupiedByWorker = occupiedByWorker;
        super.getBoardObserver().notifyBoardChange(this);
    }


    /**
     * This method returns true if the cell is occupied by a dome, false otherwise.
     * @return boolean representing if the cell is occupied by a dome or not
     */
    public boolean getOccupiedByDome() {
        return occupiedByDome;
    }


    /**
     * This method sets if the cell is occupied by a dome or not.
     * @param occupiedByDome boolean representing if the cell is occupied by a dome or not
     */
    public void setOccupiedByDome(boolean occupiedByDome) throws IOException {
        this.occupiedByDome = occupiedByDome;
        notifyBoardChange();
    }


    /**
     * This method returns the coordinate of the cell.
     * @return coordinate of the cell
     */
    public Coord getCoord() {
        return coord;
    }


    /**
     * This method sets the coordinate of the cell.
     * @param coord coordinate of the cell
     */
    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) throws IOException {
        this.color = color;
        super.getBoardObserver().notifyBoardChange(this);
    }

    /**
     * This method notifies the related observer that a change occurred and the client has to be advised of that
     * @throws IOException
     */
    public void notifyBoardChange() throws IOException {
        super.getBoardObserver().notifyBoardChange(this);
    }

    @Override
    public Cell clone() {
        return new Cell(coord.clone(), super.getBoardObserver());
    }
}