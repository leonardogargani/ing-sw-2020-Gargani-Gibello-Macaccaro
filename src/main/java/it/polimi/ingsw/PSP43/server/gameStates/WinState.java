package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;

import java.util.ArrayList;

public class WinState extends TurnState {
    String winner;

    public WinState(GameSession gameSession) {
        super(gameSession, TurnName.WIN_STATE);
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void executeState() {
        EndGameMessage messageForLosers = new EndGameMessage(winner + " win the play!", endGameHeader);
        EndGameMessage messageForTheWinner = new EndGameMessage("Congratulations! You have won the game!", endGameHeader);

        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(winner);
        super.getGameSession().sendEndingMessage(messageForLosers, messageForTheWinner, nicksExcluded);
    }
}
