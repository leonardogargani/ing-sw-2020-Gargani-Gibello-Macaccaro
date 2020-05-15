package it.polimi.ingsw.PSP43.server.model.card.behaviours.buildBehaviours;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;

import java.io.IOException;

public interface BuildBehaviour {
    void handleInitBuild(GameSession gameSession) throws IOException, ClassNotFoundException, InterruptedException, GameEndedException;
}
