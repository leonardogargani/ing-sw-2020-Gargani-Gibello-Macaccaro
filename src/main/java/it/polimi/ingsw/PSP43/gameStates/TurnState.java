package it.polimi.ingsw.PSP43.gameStates;

import it.polimi.ingsw.PSP43.clientMessages.GenericMessage;
import it.polimi.ingsw.PSP43.model.*;

public abstract class TurnState {
    private GameSession gameSession;

    public TurnState(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void handleCommand(GenericMessage message) {}
}
