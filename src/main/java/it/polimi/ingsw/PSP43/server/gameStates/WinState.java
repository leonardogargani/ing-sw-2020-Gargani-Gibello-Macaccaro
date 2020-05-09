package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;

import java.io.IOException;
import java.util.ArrayList;

public class WinState extends TurnState {
    String winner;

    public WinState(GameSession gameSession) {
        super(gameSession);
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void executeState() throws IOException, ClassNotFoundException {
        EndGameMessage message = new EndGameMessage(winner + " win the play!");
        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(winner);
        super.getGameSession().sendEndingMessage(message, nicksExcluded);
    }
}
