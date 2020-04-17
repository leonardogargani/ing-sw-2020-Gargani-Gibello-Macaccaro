package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.networkMessages.GenericMessage;

public abstract class TurnState {
    private GameSession gameSession;

    public TurnState(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void initState(GenericMessage message) {
    }

    public void initState() {
    }
}
