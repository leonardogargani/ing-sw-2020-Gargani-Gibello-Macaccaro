package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.modelHandlersException.FullGameSessionException;
import it.polimi.ingsw.PSP43.server.networkMessages.GenericMessage;
import it.polimi.ingsw.PSP43.server.model.*;

public abstract class TurnState {
    private GameSession gameSession;

    public TurnState(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void handleCommand(GenericMessage message) {
    }
}
