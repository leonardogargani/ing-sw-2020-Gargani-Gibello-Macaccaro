package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameLostException;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.WinnerCaughtException;

public interface MoveBehavior {
    void handleInitMove(GameSession gameSession) throws GameEndedException, WinnerCaughtException, GameLostException;
}
