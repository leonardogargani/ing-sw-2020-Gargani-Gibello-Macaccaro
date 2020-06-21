package it.polimi.ingsw.PSP43.client.graphic.cli;


/**
 * Exception thrown if a player writes "quit" (ignore capitalization) in the cli.
 */
public class QuitPlayerException extends Exception {

    public QuitPlayerException(String message) {
        super(message);
    }

}
