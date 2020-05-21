package it.polimi.ingsw.PSP43.server.modelHandlersException;


/**
 * Exception thrown if a card is already chosen by another player in the same game
 */
public class CardAlreadyInUseException extends Exception {
    private static final long serialVersionUID = -8216496556097861565L;

    /**
     * Non default constructor that initializes the exception with a message.
     * @param message message containing some information about the exception
     */
    public CardAlreadyInUseException(String message) {
        super(message);
    }

}
