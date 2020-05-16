package it.polimi.ingsw.PSP43.server.model.card.behaviours.moveBehaviours;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;

public interface MoveBehavior {
    void handleInitMove(GameSession gameSession) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException, GameEndedException;
}
