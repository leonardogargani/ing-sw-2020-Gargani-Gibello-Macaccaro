package it.polimi.ingsw.PSP43.server.model.card;

import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.*;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellAlreadyOccupiedExeption;
import it.polimi.ingsw.PSP43.server.modelHandlersException.CellHeightException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.util.ArrayList;

public class ConditionOnSwapDecorator implements MoveBehavior {
    private AbstractGodCard godComponent;

    public ConditionOnSwapDecorator(AbstractGodCard godComponent) {
        this.godComponent = godComponent;
    }

    public ConditionOnSwapDecorator() {
    }

    public AbstractGodCard getGodComponent() {
        return godComponent;
    }

    public void setGodComponent(AbstractGodCard godComponent) {
        this.godComponent = godComponent;
    }

    @Override
    public void handleMove(GameSession gameSession, Player player, Worker workerToMove, Coord newPosition) throws CellHeightException, CellAlreadyOccupiedExeption, WinnerCaughtException {
        CellsHandler cellsHandler = gameSession.getCellsHandler();
        WorkersHandler workersHandler = gameSession.getWorkersHandler();
        Cell cellOfOpponent = cellsHandler.getCell(newPosition);

        if (cellOfOpponent.getOccupiedByDome()) throw new CellAlreadyOccupiedExeption();

        else if (!cellOfOpponent.getOccupiedByDome() && !cellOfOpponent.getOccupiedByWorker())
            godComponent.move(gameSession, player, workerToMove, newPosition);

        else if ("Apollo".equals(player.getAbstractGodCard().getGodName())) {
            swapWorkers(workerToMove, workersHandler.getWorker(newPosition));
        }

        else {
            CellsHandler.AbstractIterator iterator = gameSession.getCellsHandler().iterator(workerToMove.getCurrentPosition(), newPosition);
            ArrayList<Cell> availablePositionsToSend = new ArrayList<>();
            Cell cellToCopy;
            while ((cellToCopy = iterator.next()) != null) {
                if (cellToCopy.getHeight() == cellOfOpponent.getHeight())
                    availablePositionsToSend.add(cellToCopy);
            }

            Worker opponentWorker = workersHandler.getWorker(newPosition);
            ClientListener receiver = gameSession.getListenersHashMap().get(player.getNickname());

            // TODO : coordChosen will be initialised with the cell chosen by the player
            Coord coordChosen = new Coord(0, 0);
            // TODO : send all the possible positions to the player

            Cell oldCell = cellsHandler.getCell(workerToMove.getCurrentPosition());
            oldCell.setOccupiedByWorker(false);
            cellsHandler.changeStateOfCell(oldCell, workerToMove.getCurrentPosition());
            workerToMove.setCurrentPosition(newPosition);

            Cell newOpponentCell = cellsHandler.getCell(coordChosen);
            newOpponentCell.setOccupiedByWorker(true);
            cellsHandler.changeStateOfCell(newOpponentCell, coordChosen);
            opponentWorker.setCurrentPosition(coordChosen);
         }
    }

    private void swapWorkers(Worker worker1, Worker worker2) {
        worker1.setCurrentPosition(worker2.getCurrentPosition());
        worker2.setCurrentPosition(worker1.getPreviousPosition());
    }
}
