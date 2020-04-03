package it.polimi.ingsw.PSP43.modelHandlersException;

/**
 * Exception thrown if a nickname chosen by a player is already in use in the same game
 */
public class NicknameAlreadyInUseException extends Exception {
    public NicknameAlreadyInUseException(String message) {
        super(message);
    }
}
