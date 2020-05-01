package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
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

    public void initState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        try {
            executeState();
        } catch (WinnerCaughtException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void executeState(ClientMessage message) throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        findNextState();
    }

    public void executeState() throws WinnerCaughtException, IOException, ClassNotFoundException, InterruptedException {
        findNextState();
    }

    public void findNextState() throws IOException, ClassNotFoundException, WinnerCaughtException, InterruptedException {
        gameSession.transitToNextState();
    }
}
