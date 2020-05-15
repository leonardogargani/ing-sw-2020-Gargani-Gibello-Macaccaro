package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;


public abstract class TurnState {
    private GameSession gameSession;

    public TurnState(GameSession gameSession) {
        this.gameSession = gameSession;
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
}
