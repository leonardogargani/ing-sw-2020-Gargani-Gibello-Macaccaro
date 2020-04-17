package it.polimi.ingsw.PSP43.server.model.card;

import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

public class DoubleMoveDecorator implements MoveBehavior {
    private AbstractGodCard godComponent;

    public DoubleMoveDecorator(AbstractGodCard godComponent) {
        this.godComponent = godComponent;
    }

    public DoubleMoveDecorator() {
    }

    public AbstractGodCard getGodComponent() {
        return godComponent;
    }

    public void setGodComponent(AbstractGodCard godComponent) {
        this.godComponent = godComponent;
    }

    @Override
    public void handleMove(GameSession gameSession, Player player, Worker workerToMove, Coord newPosition) throws CellHeightException, CellAlreadyOccupiedExeption, WinnerCaughtException {
        godComponent.move(gameSession, player, workerToMove, newPosition);
        // TODO : send a message to the client if he wants to do another move
        Coord forbiddenCell = workerToMove.getPreviousPosition();
        Coord positionChosen = null;
        // TODO : receive the message from the player with the next coord
        if (positionChosen != null) {
            godComponent.move(gameSession, player, workerToMove, positionChosen);
        }
    }
}
