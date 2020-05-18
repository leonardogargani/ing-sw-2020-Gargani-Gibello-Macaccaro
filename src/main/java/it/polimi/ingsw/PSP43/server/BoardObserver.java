package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.server.gameStates.GameSessionObservable;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.networkMessages.CellMessage;

import java.io.IOException;

public class BoardObserver {

    private final GameSessionObservable gameSessionObservable;


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
