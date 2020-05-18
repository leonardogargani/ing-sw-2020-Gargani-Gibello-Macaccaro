package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;

public abstract class TurnState {
    private final GameSession gameSession;
    private final TurnName turnName;

    public TurnState(GameSession gameSession, TurnName turnName) {
        this.gameSession = gameSession;
        this.turnName = turnName;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void initState() {
        executeState();
    }

    public void executeState(RegistrationMessage message) {
        findNextState();
    }

    public void executeState() {
        findNextState();
    }

    public void findNextState() {
    }

    public TurnName getTurnName() {
        return turnName;
    }
}
