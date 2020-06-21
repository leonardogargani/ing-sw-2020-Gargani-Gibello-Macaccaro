package it.polimi.ingsw.PSP43.server.controller.cards.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException.GameEndedException;

import java.io.IOException;

public interface BuildBehaviour {
    void handleInitBuild(GameSession gameSession) throws IOException, ClassNotFoundException, InterruptedException, GameEndedException;
}
