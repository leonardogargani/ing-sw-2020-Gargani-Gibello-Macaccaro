package it.polimi.ingsw.PSP43.gameStates;

import it.polimi.ingsw.PSP43.model.*;
import it.polimi.ingsw.PSP43.modelHandlers.WorkersHandler;

public class SwapMoveState extends TurnState {
    private GameSession currentGame;
    private TurnState turnDecorator;

    public void handleCommand(GenricMessage message) {
        WorkersHandler workersHandler = currentGame.getWorkersHandler();
        Player player = currentGame.getPlayerHandler().getPlayer(message.getIdPlayer());
        Cell oldPosition = workersHandler.getWorker(message.getIdWorker()).getCurrentPosition();
        Cell newPosition = currentGame.getCellsHandler().getCell(message.getNextPosition);
        Cell swapCell = new Cell();
        if ()
    }
}


/*  I assume that the type message sent by the player has the following content:
        - Id game;
        - Id player;
        - Id worker;
        - Next Position in Coord;
*
*/