package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;

/**
 * This class is used to contain all the useful information to move a worker.
 */
public class DataToMove {

    private final GameSession gameSession;
    private final Player player;
    private final Worker worker;
    private final Coord newPosition;


    /**
     * Noj default constructor that sets all the attributes of the object.
     * @param gameSession GameSession attribute
     * @param player Player attribute
     * @param worker Worker attribute
     * @param newPosition Coord attribute
     */
    public DataToMove(GameSession gameSession, Player player, Worker worker, Coord newPosition) {
        this.gameSession = gameSession;
        this.player = player;
        this.worker = worker;
        this.newPosition = newPosition;
    }


    /**
     * Method that returns the gameSession attribute.
     * @return gameSession attribute
     */
    public GameSession getGameSession() {
        return gameSession;
    }


    /**
     * Method that returns the player attribute.
     * @return player attribute
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Method that returns the worker attribute.
     * @return worker attribute
     */
    public Worker getWorker() {
        return worker;
    }


    /**
     * Method that returns the newPosition attribute.
     * @return newPosition attribute
     */
    public Coord getNewPosition() {
        return newPosition;
    }

}
