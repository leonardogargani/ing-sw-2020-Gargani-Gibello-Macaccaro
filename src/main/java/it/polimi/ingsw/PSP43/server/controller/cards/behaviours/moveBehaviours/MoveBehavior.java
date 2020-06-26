package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.WinnerCaughtException;

/**
 * This is the interface used to implements different behaviours of move, according to the different cards.
 */
public interface MoveBehavior {
    /**
     * This method is used to handle the move during a turn.
     * @param gameSession This is a reference to the main access to the game database.
     * @throws GameEndedException if the player decides to leave the game during his turn.
     * @throws GameLostException if the player can't do any move.
     */
    void handleInitMove(GameSession gameSession) throws GameEndedException, GameLostException;
}
