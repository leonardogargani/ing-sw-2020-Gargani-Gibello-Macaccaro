package it.polimi.ingsw.PSP43.server.model;


import it.polimi.ingsw.PSP43.server.BoardObserver;

import java.io.Serializable;

public abstract class Observable implements Serializable {
    private static final long serialVersionUID = -6752357803627291761L;

    private final transient BoardObserver boardObserver;

    public Observable(BoardObserver boardObserver) {
        this.boardObserver = boardObserver;
    }

    public BoardObserver getBoardObserver() {
        return boardObserver;
    }
}
