package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;

import java.io.IOException;


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

    public void initState() throws IOException, ClassNotFoundException, InterruptedException {
        executeState();
    }

    public void executeState(RegistrationMessage message) throws IOException, ClassNotFoundException, InterruptedException {
        findNextState();
    }

    public void executeState() throws IOException, ClassNotFoundException, InterruptedException {
        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException, InterruptedException {
    }

    public TurnName getTurnName() {
        return turnName;
    }
}
