package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;

/**
 * This is the interface used to implement all the behaviours of building blocks
 * or domes with workers.
 */
public interface BuildBehaviour {
    /**
     * This method is used to handle in different ways a build of a block or dome.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     */
    void handleInitBuild(GameSession gameSession) throws GameEndedException;
}
