package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

public class WinState extends TurnState {
    String winner;

    public WinState(GameSession gameSession) {
        super(gameSession);
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void executeState() {
        // TODO : send a message that a player won and give the order to the clientlisteners to close the sockets
    }
}
