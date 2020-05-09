package it.polimi.ingsw.PSP43.server.modelHandlersException;

public class WinnerCaughtException extends Exception {
    private String winner;

    public WinnerCaughtException(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
