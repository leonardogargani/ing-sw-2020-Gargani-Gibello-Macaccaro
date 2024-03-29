package it.polimi.ingsw.PSP43.server.model;

import java.io.Serializable;

/**
 * This class represents an object that will be sent on the network to update the view of the client.
 */
public abstract class Observable implements Serializable {

    private static final long serialVersionUID = -6752357803627291761L;

    // transient attribute since it needs to be ignored by the serialization
    private final transient BoardObserver boardObserver;


    /**
     * Non default constructor that sets the boardObserver attribute.
     * @param boardObserver boardObserver attribute that needs to be set
     */
    public Observable(BoardObserver boardObserver) {
        this.boardObserver = boardObserver;
    }


    /**
     * Method that returns the boardObserver attribute.
     * @return boardObserver attribute
     */
    public BoardObserver getBoardObserver() {
        return boardObserver;
    }

}
