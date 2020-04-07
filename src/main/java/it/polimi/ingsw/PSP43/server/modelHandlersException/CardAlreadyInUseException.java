package it.polimi.ingsw.PSP43.server.modelHandlersException;

/**
 * Exception thrown if a card is already chosen by another player in the same game
 */
public class CardAlreadyInUseException extends Exception {
    public CardAlreadyInUseException(String message) {
        super(message);
    }
}
