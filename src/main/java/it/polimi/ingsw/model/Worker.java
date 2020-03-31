package it.polimi.ingsw.model;


/**
 * Worker is the class that represents a worker, which is the piece players can control.
 * Every player has two workers, both of the same color and able to move their position
 * during the game.
 */
public class Worker {

    private Cell currentPosition;
    private Cell previousPosition;
    private String color;


    /**
     * Non-default constructor, it initializes a worker with its color, chosen by the owner.
     * @param color color of the worker
     */
    public Worker(String color) {
        this.currentPosition = null;
        this.previousPosition = null;
        this.color = color;
    }


    /**
     * This method returns the position the worker is in at this moment.
     * @return the position the worker is in at this moment
     */
    public Cell getCurrentPosition() {
        return currentPosition;
    }  // DEVO RITORNARE UNA COPIA DI CELL


    /**
     * This method sets the position the worker is in at this moment.
     * @param currentPosition the position the worker is in at this moment
     */
    public void setCurrentPosition(Cell currentPosition) {  // DEVO PASSARE UNA COPIA DI CELL
        this.previousPosition = this.currentPosition;
        this.currentPosition = currentPosition;
    }


    /**
     * This method returns the position the worker was before its last move.
     * @return position the worker was before its last move
     */
    public Cell getPreviousPosition() {
        return previousPosition;
    }  // DEVO RITORNARE UNA COPIA DI CELL


    /**
     * This method returns the color of the worker.
     * @return color of the worker
     */
    public String getColor() {
        return color;
    }


}
