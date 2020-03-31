package it.polimi.ingsw.model;

/**
 * Worker description arriving from the net and traslated into a message for a specified GameSession
 */
public class WorkerDescriptor extends GenericDescriptor {
    private Worker workerReference;
    private Cell nextPosition;
    private String color;

    /**
     *
     * @param gameIdentifier Identification of the object GameSession to which the message is to be sent and handled
     * @param workerRef Reference to the worker data to modify
     * @param nextPosition Position in which the player wants to place his worker
     * @param color Color of the worker
     */
    public WorkerDescriptor(GameSession gameIdentifier, Worker workerRef, Cell nextPosition, String color) {
        super(gameIdentifier);
        this.workerReference = workerRef;
        this.nextPosition = nextPosition;
        this.color = color;
    }

    /**
     *
     * @return The worker of the description
     */
    public Worker getWorker() {
        return workerReference;
    }

    /**
     *
     * @return The cell in which is placed the worker of the description
     */
    public Cell getNextPosition() {
        return nextPosition;
    }

    /**
     *
     * @return The color of the worker of the description
     */
    public String getColor() {
        return color;
    }
}
