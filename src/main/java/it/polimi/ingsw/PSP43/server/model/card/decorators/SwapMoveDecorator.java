package it.polimi.ingsw.PSP43.server.model.card.decorators;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.server.model.DataToMove;
import it.polimi.ingsw.PSP43.server.gameStates.GameSession;
import it.polimi.ingsw.PSP43.server.model.Cell;
import it.polimi.ingsw.PSP43.server.model.Coord;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.model.Worker;
import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.modelHandlers.CellsHandler;
import it.polimi.ingsw.PSP43.server.modelHandlers.WorkersHandler;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.modelHandlersException.WinnerCaughtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SwapMoveDecorator extends PowerGodDecorator {
    private static final long serialVersionUID = 1282873326963180012L;

    public SwapMoveDecorator() {
        super();
    }

    public SwapMoveDecorator(AbstractGodCard godComponent) {
        super(godComponent);
    }

    @Override
    public void initMove(GameSession gameSession) throws ClassNotFoundException, WinnerCaughtException, InterruptedException, IOException, GameEndedException {
        ActionResponse actionResponse = askForMove(gameSession, findAvailablePositionsToMove(gameSession));

        Worker workerMoved = gameSession.getWorkersHandler().getWorker(actionResponse.getWorkerPosition());
        move(new DataToMove(gameSession, gameSession.getCurrentPlayer(), workerMoved, actionResponse.getPosition()));
    }

    public void move(DataToMove dataToMove) throws IOException, WinnerCaughtException, InterruptedException, ClassNotFoundException {
        CellsHandler cellsHandler = dataToMove.getGameSession().getCellsHandler();
        Coord newCoord = dataToMove.getNewPosition();

        if (!cellsHandler.getCell(newCoord).getOccupiedByWorker())
            super.move(dataToMove);
        else {
            WorkersHandler handler = dataToMove.getGameSession().getWorkersHandler();
            Worker opponentWorker = handler.getWorker(dataToMove.getNewPosition());
            swapWorkers(handler, dataToMove.getWorker(), opponentWorker);
        }
    }

    public HashMap<Coord, ArrayList<Coord>> findAvailablePositionsToMove(GameSession gameSession) {
        CellsHandler cellsHandler = gameSession.getCellsHandler();

        HashMap<Coord, ArrayList<Coord>> availablePositions = cellsHandler.findWorkersNeighbouringCoords(gameSession.getCurrentPlayer());

        for (Coord actualCoord : availablePositions.keySet()) {
            Cell actualCell = cellsHandler.getCell(actualCoord);
            ArrayList<Coord> neighbouringPositions = availablePositions.get(actualCoord);
            for (Iterator<Coord> coordIterator = neighbouringPositions.iterator(); coordIterator.hasNext(); ) {
                Coord coordToCheck = coordIterator.next();
                Cell cellToCheck = cellsHandler.getCell(coordToCheck);
                if (cellToCheck.getOccupiedByDome() || (cellToCheck.getHeight() - actualCell.getHeight() > 1))
                    coordIterator.remove();
            }
        }
        return availablePositions;
    }

    private void swapWorkers(WorkersHandler workersHandler, Worker worker1, Worker worker2) throws IOException {
        workersHandler.swapPositions(worker1, worker2);
    }

    public AbstractGodCard cleanFromEffects(String nameOfEffect) throws ClassNotFoundException {
        AbstractGodCard component = super.getGodComponent().cleanFromEffects(nameOfEffect);
        Class<?> c = Class.forName(nameOfEffect);
        if (!c.isInstance(this))
            return new SwapMoveDecorator(component);
        else return component;
    }
}
