package it.polimi.ingsw.PSP43.server.model;

import it.polimi.ingsw.PSP43.server.controller.gameStates.GameSessionObservable;
import it.polimi.ingsw.PSP43.server.network.networkMessages.CellMessage;

/**
 * This class is responsible for observing all the cells of the board of the Game and to notify
 * the players if there was a change on them.
 */
public class BoardObserver {
    private final GameSessionObservable gameSessionObservable;

    /**
     * Non default constructor that initializes the gameSessionObservable attribute.
     * @param gameSessionObservable gameSessionObservable attribute
     */
    public BoardObserver(GameSessionObservable gameSessionObservable) {
        this.gameSessionObservable = gameSessionObservable;
    }

    /**
     * This method receives a cell that has changed its state in the model,
     * and notifies the client in order to represent the graphical change.
     * @param cellObserved cellObserved object of the board
     */
    public void notifyBoardChange(Cell cellObserved) {
        CellMessage message = new CellMessage(cellObserved);
        gameSessionObservable.sendBroadCast(message);
    }
}
