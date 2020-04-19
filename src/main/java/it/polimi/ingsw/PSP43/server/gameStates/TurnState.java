package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;
import it.polimi.ingsw.PSP43.server.networkMessages.GenericMessage;


public abstract class TurnState {
    private GameSession gameSession;

    public TurnState(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void initState() {
        try {
            executeState();
        } catch (WinnerCaughtException e) {
            e.printStackTrace();
        }
    }

    public void executeState() throws WinnerCaughtException {
        findNextState();
    }

    public void executeState(GenericMessage message) {
        findNextState();
    }

    public void findNextState() {
        gameSession.transitToNextState();
    }
}
