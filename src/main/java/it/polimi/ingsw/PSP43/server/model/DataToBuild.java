package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;


public class DataToBuild extends DataToMove {

    private final Boolean buildDome;


    /**
     * Non default constructor that sets all the attributes invoking also the DataToMove constructor.
     * @param gameSession GameSession attribute to be set by DataToMove constructor
     * @param player Player attribute to be set by DataToMove constructor
     * @param worker Worker attribute to be set by DataToMove constructor
     * @param newPosition Coord attribute to be set by DataToMove constructor
     * @param buildDome Boolean attribute by this constructor
     */
    public DataToBuild(GameSession gameSession, Player player, Worker worker, Coord newPosition, Boolean buildDome) {
        super(gameSession, player, worker, newPosition);
        this.buildDome = buildDome;
    }


    /**
     * Method that returns the buildDome attribute.
     * @return buildDome attribute
     */
    public Boolean getBuildDome() {
        return buildDome;
    }

}
