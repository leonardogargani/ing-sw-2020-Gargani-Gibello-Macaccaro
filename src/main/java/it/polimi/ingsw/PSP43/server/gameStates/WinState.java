package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;

import java.util.ArrayList;

/**
 * This is the State of the game where the winner of the game is told to all the players.
 */
public class WinState extends TurnState {
    protected String winner;

    public WinState(GameSession gameSession) {
        super(gameSession, TurnName.WIN_STATE);
    }

    /**
     * This method is called externally to set a winner for the game.
     * @param winner This is the name of the winner of the game.
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * This method creates the messages for the winner and for the losers. Then it calls a method to send
     * the ending message and delegates to it the duty to stop all the threads of the game.
     */
    public void executeState() {
        EndGameMessage messageForLosers = new EndGameMessage(winner, EndGameMessage.EndGameHeader.LOSER);
        EndGameMessage messageForTheWinner = new EndGameMessage(null, EndGameMessage.EndGameHeader.WINNER);

        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(winner);
        super.getGameSession().sendEndingMessage(messageForLosers, messageForTheWinner, nicksExcluded);
    }
}