package it.polimi.ingsw.PSP43.server.controller.modelHandlers.modelHandlersException;


/**
 * Exception thrown if a nickname chosen by a player is already in use in the same game
 */
public class NicknameAlreadyInUseException extends Exception {
    private static final long serialVersionUID = 9098529835369783424L;

    /**
     * Non default constructor that initializes the exception with a message.
     * @param message message containing some information about the exception
     */
    public NicknameAlreadyInUseException(String message) {
        super(message);
    }

}
