package it.polimi.ingsw.PSP43.server.modelHandlersException;


public class WinnerCaughtException extends Exception {
    private static final long serialVersionUID = 4120386884141385141L;
    private final String winner;


    /**
     * Non default constructor that initializes the exception with the name of the winner.
     * @param winner String containing the name of the winner
     */
    public WinnerCaughtException(String winner) {
        this.winner = winner;
    }


    /**
     * Method that returns the name of the winner.
     * @return name of the winner
     */
    public String getWinner() {
        return winner;
    }
}
