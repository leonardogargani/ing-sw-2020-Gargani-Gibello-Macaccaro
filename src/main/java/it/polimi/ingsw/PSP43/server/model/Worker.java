package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.Color;
import it.polimi.ingsw.PSP43.server.BoardObserver;


/**
 * Worker is the class that represents a worker, which is the piece players can control.
 * Every player has two workers, both of the same color and able to move their position
 * during the game.
 */
public class Worker {

    private final int id;
    private Coord currentPosition;
    private Coord previousPosition;
    private final Color color;
    private boolean latestMoved;


    /**
     * Non-default constructor, it initializes a worker with its color, chosen by the owner.
     * @param id id of the worker
     * @param color color of the worker
     * @param boardObserver boardObserver of the game
     */
    public Worker(int id, Color color, BoardObserver boardObserver) {
        this.id = id;
        this.currentPosition = null;
        this.previousPosition = null;
        this.color = color;
        this.latestMoved = false;
    }


    /**
     * This method returns the position the worker is in at this moment.
     * @return the position the worker is in at this moment
     */
    public Coord getCurrentPosition() {
        return currentPosition;
    }


    /**
     * This method sets the position the worker is in at this moment.
     * @param currentPosition the position the worker is in at this moment
     */
    public void setCurrentPosition(Coord currentPosition) {
        this.previousPosition = this.currentPosition;
        this.currentPosition = currentPosition;
    }


    /**
     * This method returns the position the worker was before its last move.
     * @return position the worker was before its last move
     */
    public Coord getPreviousPosition() {
        return previousPosition;
    }


    /**
     * This method returns the color of the worker.
     * @return color of the worker
     */
    public Color getColor() {
        return color;
    }


    /**
     * This method returns the unique id of the worker.
     * @return unique id of the worker
     */
    public int getId() {
        return id;
    }


    /**
     * This method returns if the worker has been moved in the latest turn by the player.
     * @return if the worker has been moved in the latest turn by the player
     */
    public boolean isLatestMoved() {
        return latestMoved;
    }


    /**
     * This method sets the boolean representing if a worker was moved in the last turn by the player.
     * @param latestMoved sets the boolean representing if a worker was moved in the last turn by the player
     */
    public void setLatestMoved(boolean latestMoved) {
        this.latestMoved = latestMoved;
    }

}
