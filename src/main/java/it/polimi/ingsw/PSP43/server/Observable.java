package it.polimi.ingsw.PSP43.server;


import java.io.Serializable;

public abstract class Observable implements Serializable {
    private static final long serialVersionUID = -6752357803627291761L;

    private BoardObserver boardObserver;

    public Observable(BoardObserver boardObserver) {
        this.boardObserver = boardObserver;
    }

    public void notifyBoardChange() {
        boardObserver.notifyBoardChange(this);
    }
}
